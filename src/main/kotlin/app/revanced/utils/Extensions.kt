package app.revanced.utils

import app.revanced.signature.MethodSignature
import app.revanced.signature.SignatureGenerator
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.jf.dexlib2.analysis.UnresolvedOdexInstruction
import org.jf.dexlib2.iface.Method
import org.jf.dexlib2.iface.instruction.Instruction
import org.jf.dexlib2.iface.instruction.formats.*
import org.jf.dexlib2.iface.reference.*

internal operator fun JsonObject.set(k: String, v: String) =
    this.addProperty(k, v)

internal operator fun JsonObject.set(k: String, v: JsonElement) =
    this.add(k, v)

internal fun Reference.toStr() =
    when (this) {
        is FieldReference -> "${this.definingClass}->${this.name}:${this.type}"
        is MethodReference -> "${this.definingClass}->${this.name}(${this.parameterTypes.joinToString("")})${this.returnType}"
        is TypeReference -> this.type
        is StringReference -> this.string
        is MethodProtoReference -> "methodProto(${this.parameterTypes.joinToString(", ")})${this.returnType}"
        else -> throw IllegalArgumentException("Unknown reference: ${this.javaClass.name}")
    }

internal fun Method.toStr() = SignatureGenerator.toJson(
    MethodSignature(
        this.name,
        this.returnType,
        this.accessFlags,
        this.parameterTypes.map {
            if (it.startsWith("L")) "\\[*L".toRegex().find(it)!!.value else it.toString()
        },
        this.implementation!!.instructions.map { it.opcode }
    )
)

internal fun Iterable<Instruction>.parse(sw: StringBuilder) {
    val calc = ControlFlowHelper(this)
    forEachIndexed { index, it ->
        sw.append("${index.toString().padEnd(3, ' ')} | ${it.opcode.name.padEnd(20, ' ')} | ").append(
            when (it) {
                is Instruction10x -> ""
                is Instruction10t -> "${index + calc.mapCodeAddressToRelativeIndex(it.codeOffset)}"
                is Instruction11n -> "v${it.registerA}, ${it.wideLiteral}"
                is Instruction11x -> "v${it.registerA}"
                is Instruction12x -> "v${it.registerA}, v${it.registerB}"
                is Instruction20t -> "${index + calc.mapCodeAddressToRelativeIndex(it.codeOffset)}"
                is Instruction21c -> "v${it.registerA}, ${it.reference.toStr()}"
                is Instruction21ih -> "v${it.registerA},${it.narrowLiteral}"
                is Instruction21lh -> "v${it.registerA},${it.wideLiteral}"
                is Instruction21s -> "v${it.registerA}, ${it.narrowLiteral}"
                is Instruction21t -> "v${it.registerA}, ${index + calc.mapCodeAddressToRelativeIndex(it.codeOffset)}"
                is Instruction22b -> "v${it.registerA}, v${it.registerB}, ${it.narrowLiteral}"
                is Instruction22c -> "v${it.registerA}, v${it.registerB}, ${it.reference.toStr()}"
                is Instruction22cs -> "v${it.registerA}, v${it.registerB}, ${it.fieldOffset}"
                is Instruction22s -> "v${it.registerA}, v${it.registerB}, ${it.narrowLiteral}"
                is Instruction22t -> "v${it.registerA}, v${it.registerB}, ${index + calc.mapCodeAddressToRelativeIndex(it.codeOffset)}"
                is Instruction22x -> "v${it.registerA}, v${it.registerB}"
                is Instruction23x -> "v${it.registerA}, v${it.registerB}, v${it.registerC}"
                is Instruction30t -> "${index + calc.mapCodeAddressToRelativeIndex(it.codeOffset)}"
                is Instruction31c -> "v${it.registerA}, ${it.reference.toStr()}"
                is Instruction31i -> "v${it.registerA},${it.narrowLiteral}"
                is Instruction31t -> "v${it.registerA}, ${index + calc.mapCodeAddressToRelativeIndex(it.codeOffset)}"
                is Instruction32x -> "v${it.registerA}, v${it.registerB}"
                is Instruction35c -> "{ v${it.registerC}, v${it.registerD}, v${it.registerE}, v${it.registerF}, v${it.registerG} }, ${it.reference.toStr()}"
                is Instruction35mi -> "v${it.registerC}, v${it.registerD}, v${it.registerE}, v${it.registerF}, v${it.registerG}, ${it.inlineIndex}"
                is Instruction35ms -> "v${it.registerC}, v${it.registerD}, v${it.registerE}, v${it.registerF}, v${it.registerG}, ${it.vtableIndex}"
                is Instruction3rc -> "{ ${it.startRegister} }, ${it.reference.toStr()}"
                is Instruction3rmi -> "${it.startRegister}, ${it.inlineIndex}"
                is Instruction3rms -> "${it.startRegister}, ${it.vtableIndex}"
                is Instruction45cc -> "v${it.registerC}, v${it.registerD}, v${it.registerE}, v${it.registerF}, v${it.registerG}}, ${it.reference.toStr()}, ${it.reference2.toStr()}"
                is Instruction4rcc -> "${it.startRegister}, ${it.reference.toStr()}, ${it.reference2.toStr()}"
                is ArrayPayload -> TODO()
                is PackedSwitchPayload -> TODO()
                is SparseSwitchPayload -> TODO()
                is UnresolvedOdexInstruction -> TODO()
                else -> throw IllegalArgumentException("Unknown instruction: ${it.opcode.name}")
            }
        )
        sw.append("\n")
    }
}
