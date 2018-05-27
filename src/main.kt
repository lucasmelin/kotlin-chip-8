import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.FileInputStream

fun main(args: Array<String>) {
    //Set up render system

    // Initialize CHIP-8 and load game rom into memory
    val vm = loadRom("roms/demos/Maze (alt) [David Winter, 199x].ch8")
    println(vm.memory[0x200])
    disassemble(vm)

    // Emulation loop cycle by cycle
}

fun loadRom(file: String): VM {
    // Open the rom in binary mode
    return DataInputStream(BufferedInputStream(FileInputStream(file))).use {
        val state = VM()
        val rom = it.readBytes()
        // Copy the entire rom array into state.memory starting the program counter
        System.arraycopy(rom, 0, state.memory, state.pc, rom.size)
        state
    }
}

// Disassemble the entire rom in memory
fun disassemble(vm: VM): String {
    val decoder = Disassembler()
    for (address in 0x200..(0x200+vm.romSize - 1) step 2){
        val msb = vm.memory[address]
        val lsb = vm.memory[address + 1]
        decoder.decode(address, msb, lsb)
    }
    return decoder.toString()
}

fun Decoder.decode(address: Int, msb: Byte, lsb: Byte){
    val opcode = (msb.toInt() shl 8 or lsb.toInt().and(0xFF)).and(0xFFFF) // Recombine the msb and lsb
    when (msb.hi){
        // Start matching opcodes
            0x0 -> {
                when (msb.toInt() shl 8 or lsb.toInt()) {
                    0x00E0 -> cls()
                }
            }
    }
}

val Byte.i: Int get() = this.toInt()
val Byte.lo: Int get() = this.i and 0xF // Bitmask to get low byte
val Byte.hi: Int get() = (this.i and 0xF0) shr 4 // Bitmask and shift to get the high byte
val Byte.hex: String get() = Integer.toHexString(this.i)
val Int.hex: String get() = Integer.toHexString(this)
