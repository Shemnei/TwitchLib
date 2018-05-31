package twitchlib.util

import org.json.JSONArray
import org.json.JSONObject
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.isSubclassOf

interface JsonModel {
    val root: JSONObject
}

// use \\ to go into object
fun <T : Any> json(
        propName: String? = null,
        interpretAsPath: Boolean = true,
        transform: ((Any) -> T)? = null
) = object : ReadOnlyProperty<JsonModel, T> {

    override fun getValue(thisRef: JsonModel, property: KProperty<*>): T {

        var key: String = propName ?: property.name
        var currentJObj = thisRef.root

        if (interpretAsPath) {
            val keyParts: List<String> = (key).split("\\")

            (0..keyParts.size - 2).forEach {
                key = keyParts[it]
                if (!currentJObj.has(key))
                    throw IllegalArgumentException("Property[$key] of ${thisRef::class.simpleName}::${property.name}($key) does not exist")
                currentJObj = currentJObj.getJSONObject(keyParts[it])
            }
            key = keyParts.last()
        }

        if (!currentJObj.has(key))
            throw IllegalArgumentException("Property[$key] of ${thisRef::class.simpleName}::${property.name}(\"$key\") does not exist")
        val value: Any = currentJObj.get(key)

        return if (transform != null) {
            transform(value)
        } else {
            val tKlass: KClass<*> by lazy { property.returnType.classifier as KClass<*> }
            val tListKlass: KClass<*>? by lazy {
                property.returnType.arguments.getOrNull(0)?.type?.classifier as KClass<*>?
            }

            if (value is JSONArray) {
                if (tListKlass?.isSubclassOf(JsonModel::class) == true) {
                    value.map {
                        tListKlass.constructors.first().call(it as JSONObject)
                    } as T
                } else {
                    value.toList() as T
                }
            } else if (value is JSONObject && tKlass.isSubclassOf(JsonModel::class)) {
                tKlass.constructors.first().call(value) as T
            } else {
                value as T
            }
        }
    }
}