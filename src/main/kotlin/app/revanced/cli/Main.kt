package app.revanced.cli

import app.revanced.utils.Clipboard
import app.revanced.utils.parse
import app.revanced.utils.toStr
import lanchon.multidexlib2.BasicDexFileNamer
import lanchon.multidexlib2.MultiDexIO
import org.jf.dexlib2.Opcodes
import java.io.File

fun main(args: Array<String>) {
    val dexFile = MultiDexIO.readDexFile(true, File(args[0]), BasicDexFileNamer(), Opcodes.getDefault(), null)
    while (true) {
        print("Format: type method\n> ")
        val input = readln()
        if (!input.contains(" ")) break
        val (className, methodName) = input.split(" ")

        outer@ for (classDef in dexFile.classes) {
            if (!classDef.type.contains(className)) continue

            for (method in classDef.methods) {
                if (!method.name.contains(methodName)) continue
                if (method.implementation == null) continue

                println("Method (${method.implementation!!.registerCount} registers): ${method.definingClass}->${method.name}(${method.parameterTypes.joinToString("")})${method.returnType}\n")

                // print out the instructions
                val sw = StringBuilder()
                method.implementation!!.instructions.parse(sw)
                println(sw.toString())

                // create a signature
                val signature = method.toStr()
                // save signature to clipboard
                Clipboard.saveContent(signature)

                break@outer
            }
        }
    }
}

