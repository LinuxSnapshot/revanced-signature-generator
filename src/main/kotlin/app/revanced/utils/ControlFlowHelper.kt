package app.revanced.utils

import org.jf.dexlib2.iface.instruction.Instruction


internal class ControlFlowHelper(
    instructions: Iterable<Instruction>
) {
    private val codeAddressList = mutableListOf<Int>()
    private var index = 0

    init {
        var addr = 0
        for (instruction in instructions) {
            addr += instruction.codeUnits
            codeAddressList.add(addr)
        }
        val avgCodeUnitsPerInstruction = 1.9f
        index = (addr / avgCodeUnitsPerInstruction).toInt()
        val size = instructions.count()
        if (index >= size) {
            index = size - 1
        }
    }


    internal fun mapCodeAddressToRelativeIndex(codeAddress: Int): Int {
        return codeAddress // TODO: fix below
        val guessedLocation: Int = codeAddressList[index]
        return if (guessedLocation == codeAddress) {
            index
        } else if (guessedLocation > codeAddress) {
            do {
                index--
            } while (codeAddressList.elementAt(index) > codeAddress)
            index
        } else {
            do {
                index++
            } while (index < codeAddressList.size && codeAddressList.elementAt(index) <= codeAddress)
            index - 1
        }
    }
}
