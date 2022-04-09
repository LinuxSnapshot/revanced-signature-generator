package app.revanced.signature

import app.revanced.util.set
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.jf.dexlib2.AccessFlags

internal object SignatureGenerator {
    private val json: Gson = GsonBuilder().setPrettyPrinting().create()
    private var index = 0

    internal fun toJson(signature: MethodSignature): String {
        val o = JsonObject()
        index++

        o["name"] = "func$index"
        o["returns"] = signature.returnType

        val accessors = JsonArray()
        AccessFlags.getAccessFlagsForMethod(signature.accessFlags).forEach {
            accessors.add(it.toString())
        }
        o["accessors"] = accessors

        val parameters = JsonArray()
        signature.methodParameters.forEach {
            parameters.add(it)
        }
        o["parameters"] = parameters

        val opcodes = JsonArray()
        signature.opcodes.forEach { opcodes.add(it.name) }
        o["opcodes"] = opcodes

        return json.toJson(o)
    }
}
