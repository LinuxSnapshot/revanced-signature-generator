package app.revanced.signature

import org.jf.dexlib2.Opcode

@Suppress("ArrayInDataClass")
internal data class MethodSignature(
    val name: String,
    val returnType: String,
    val accessFlags: Int,
    val methodParameters: List<String>,
    val opcodes: List<Opcode>
)