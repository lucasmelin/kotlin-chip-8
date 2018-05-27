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
        val rom = it.readBytes()
        val state = VM(romSize = rom.size)
        // Copy the entire rom array into state.memory starting the program counter
        System.arraycopy(rom, 0, state.memory, state.pc, rom.size)
        state
    }
}

// Disassemble the entire rom in memory
fun disassemble(vm: VM): String {
    val decoder = Disassembler()
    for (address in 0x200..(0x200 + vm.romSize - 1) step 2) {
        val msb = vm.memory[address]
        val lsb = vm.memory[address + 1]
        decoder.decode(address, msb, lsb)
    }
    return decoder.toString()
}

fun Decoder.decode(address: Int, msb: Byte, lsb: Byte) {
    val opcode = (msb.toInt() shl 8 or lsb.toInt().and(0xFF)).and(0xFFFF) // Recombine the msb and lsb
    when (msb.hi) {
    // Start matching opcodes
        0x0 -> {
            when (msb.toInt() shl 8 or lsb.toInt()) {
                0x00E0 -> cls()
                0x00EE -> ret()
            }
        }
        0x1 -> jp(address(msb, lsb))
        0x2 -> call(address(msb, lsb))
        0x3 -> se(msb.lo, lsb.toInt())
        0x4 -> sne(msb.lo, lsb.toInt())
        0x5 -> ser(msb.lo, lsb.toInt())
        0x6 -> set(msb.lo, lsb.toInt())
        0x7 -> add(msb.lo, lsb.toInt())
        0x8 -> {
            // Get both registers
            val registerX = msb.lo
            val registerY = lsb.hi
            when (lsb.lo){
                0x0 -> setr(registerX, registerY)
                0x1 -> or(registerX, registerY)
                0x2 -> and(registerX, registerY)
                0x3 -> xor(registerX, registerY)
                0x4 -> addr(registerX, registerY)
                0x5 -> sub(registerX, registerY)
                0x6 -> shr(registerY)
                0x7 -> subt(registerX, registerY)
                0xE -> shl(registerY)
            }
        }
        0x9 -> {
            // Get both registers
            val registerX = msb.lo
            val registerY = lsb.hi
            sner(registerX, registerY)
        }
        0xA -> seti(address(msb, lsb))
        0xB -> jpv0(address(msb, lsb))
        0xC -> rand(msb.lo, lsb.toInt())
        0xD -> draw(msb.lo, lsb.hi, lsb.lo)
        0xE -> {
            when (lsb.toInt() or 0xFF) {
                0x9E -> skey(msb.lo)
                0xA1 -> snkey(msb.lo)
            }
        }
        0xF -> {
            val register = msb.lo
            when (lsb.toInt() or 0xFF){
                0x07 -> getdelay(register)
                0x0A -> waitkey(register)
                0x15 -> setdelay(register)
                0x18 -> setsound(register)
                0x1E -> addi(register)
                0x29 -> spritei(register)
                0x33 -> bcd(register)
                0x55 -> push(register)
                0x65 -> pop(register)
            }
        }
    }
}

val Byte.i: Int get() = this.toInt()
val Byte.lo: Int get() = this.i and 0xF // Bitmask to get low byte
val Byte.hi: Int get() = (this.i and 0xF0) shr 4 // Bitmask and shift to get the high byte
val Byte.hex: String get() = Integer.toHexString(this.i)
val Int.hex: String get() = Integer.toHexString(this)
fun address(msb: Byte, lsb: Byte) = ((msb.toInt() and 0xF) shl 8) or (lsb.toInt() and 0xFF) // Combine msb and lsb into a 12-bit address
