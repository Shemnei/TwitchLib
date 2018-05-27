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

fun <T : Any> json(propName: String? = null, transform: ((Any) -> T)? = null) = object : ReadOnlyProperty<JsonModel, T> {
    override fun getValue(thisRef: JsonModel, property: KProperty<*>): T {
        val key: String = propName ?: property.name
        if (!thisRef.root.has(key))
            throw IllegalArgumentException("Property[$key] of ${thisRef::class.simpleName}::${property.name} does not exist")
        val value: Any = thisRef.root.get(key)

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