import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.FileInputStream
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.experimental.xor

fun main(args: Array<String>) {
    //Set up render system

    // Initialize CHIP-8 and load game rom into memory
    val vm = loadRom("roms/demos/Maze (alt) [David Winter, 199x].ch8")
    // Dump the contents of the rom
    print(disassemble(vm))

    // Execute the contents of the rom
    vm.runrom()

    // Emulation loop cycle by cycle
}

fun VM.runrom() {
    // Fetch the opcode at the program counter position
    val msb = memory[pc]
    val lsb = memory[pc + 1]

    when (msb.hi) { // Check the first nibble to determine opcode
        0x0 -> {
            when (msb.toInt() shl 8 or lsb.toInt()) {
                0x00E0 -> TODO() //cls()
                0x00EE -> TODO() //ret()
            }
        }
        0x1 -> {//jp(address(msb, lsb))
            pc = address(msb, lsb)
        }
        0x2 -> { // call
            stack[sc] = pc
            sc++
            pc = address(msb, lsb)
        }
        0x3 -> { //se(msb.lo, lsb.toInt())
            if (registers[msb.lo] == lsb){
                pc += 2
            }
        }
        0x4 -> {//sne(msb.lo, lsb.toInt())
            if (registers[msb.lo] != lsb){
                pc += 2
            }
        }
        0x5 -> {//ser(msb.lo, lsb.toInt())
            if(registers[msb.lo] == registers[lsb.hi]){
                pc += 2
            }
        }
        0x6 -> { // ld VX to NN
            registers[msb.lo] = lsb
            pc += 2
            println("ld v${msb.lo} with $lsb")
        }
        0x7 -> { // add NN to VX
            registers[msb.lo] = registers[msb.lo] and lsb
            pc += 2
            println("add ${lsb.toInt()} to v${msb.lo}")
        }
        0x8 -> {
            // Get both registers
            val registerX = msb.lo
            val registerY = lsb.hi
            when (lsb.lo){
                0x0 -> {//setr(registerX, registerY)
                    registers[registerX] = registers[registerY]
                }
                0x1 -> {//or(registerX, registerY)
                    registers[registerX] or registers[registerY]
                }
                0x2 ->{//and(registerX, registerY)
                    registers[registerX] and registers[registerY]
                }
                0x3 -> {//xor(registerX, registerY)
                    registers[registerX] xor registers[registerY]
                }
                0x4 -> TODO() //addr(registerX, registerY)
                0x5 -> TODO() //sub(registerX, registerY)
                0x6 -> TODO() //shr(registerY)
                0x7 -> TODO() //subn(registerX, registerY)
                0xE -> TODO() //shl(registerY)
                else -> TODO() //unknown(opcode, address)
            }
        }
        0x9 -> {
            // Get both registers
            val registerX = msb.lo
            val registerY = lsb.hi
            TODO() //sner(registerX, registerY)
        }
        0xA -> { // ldi
            I = address(msb, lsb)
            pc += 2
            println("I set to ${address(msb, lsb)}")
        }
        0xB -> TODO() //jpv0(address(msb, lsb))
        0xC -> TODO() //rnd(msb.lo, lsb.toInt())
        0xD -> { // drw
            registers[0xF] = 0
            // lsb.lo is height
            for (y in 0..(lsb.lo - 1)){
                val line = memory[I + y]
                for (x in 0..7){
                    val pixel = (line and ((0x80 shr x).toByte())).toInt()
                    if (pixel != 0){
                        val totalX = msb.lo + x
                        val totalY = lsb.hi + y
                        val index = totalY * 64 + totalX
                        if (display[index].toInt() == 1) {
                            registers[0xF] = 1
                        }
                        display[index] xor 1
                    }
                }
            }
        }
        0xE -> {
            when (lsb.toInt() or 0xFF) {
                0x9E -> TODO() //skp(msb.lo)
                0xA1 -> TODO() //sknp(msb.lo)
                else -> TODO() //unknown(opcode, address)
            }
        }
        0xF -> {
            val register = msb.lo
            when (lsb.toInt() or 0xFF){
                0x07 -> TODO() //getdelay(register)
                0x0A -> TODO() //waitkey(register)
                0x15 -> TODO() //setdelay(register)
                0x18 -> TODO() //setsound(register)
                0x1E -> TODO() //addi(register)
                0x29 -> TODO() //spritei(register)
                0x33 -> TODO() //bcd(register)
                0x55 -> TODO() //push(register)
                0x65 -> TODO() //pop(register)
                else -> TODO() //unknown(opcode, address)
            }
        }
        else -> TODO() //unknown(opcode, address)
    }

}

/**
 * Receives the path to a CHIP-8 rom and copies into VM memory stating at the
 * program counter index.
 */
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
        // Increment the pc by 2 for every executed opcode
        // If skip, increase pc by 4
        decoder.decode(address, msb, lsb)
    }
    return decoder.toString()
}



fun Decoder.decode(address: Int, msb: Byte, lsb: Byte){
    // Recombine the msb and lsb. msb << 8 | lsb
    val opcode = (msb.toInt() shl 8 or lsb.toInt().and(0xFF)).and(0xFFFF)
    when (msb.hi) {
        // Check the first nibble to determine opcode
        0x0 -> {
            when (msb.toInt() shl 8 or lsb.toInt()) {
                0x00E0 -> cls()
                0x00EE -> ret()
                else -> unknown(opcode, address)
            }
        }
        0x1 -> jp(address(msb, lsb))
        0x2 -> call(address(msb, lsb))
        0x3 -> se(msb.lo, lsb.toInt())
        0x4 -> sne(msb.lo, lsb.toInt())
        0x5 -> ser(msb.lo, lsb.toInt())
        0x6 -> ld(msb.lo, lsb.toInt())
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
                0x7 -> subn(registerX, registerY)
                0xE -> shl(registerY)
                else -> unknown(opcode, address)
            }
        }
        0x9 -> {
            // Get both registers
            val registerX = msb.lo
            val registerY = lsb.hi
            sner(registerX, registerY)
        }
        0xA -> ldi(address(msb, lsb))
        0xB -> jpv0(address(msb, lsb))
        0xC -> rnd(msb.lo, lsb.toInt())
        0xD -> drw(msb.lo, lsb.hi, lsb.lo)
        0xE -> {
            when (lsb.toInt() or 0xFF) {
                0x9E -> skp(msb.lo)
                0xA1 -> sknp(msb.lo)
                else -> unknown(opcode, address)
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
                else -> unknown(opcode, address)
            }
        }
        else -> unknown(opcode, address)
    }

}

val Byte.i: Int get() = this.toInt()
val Byte.lo: Int get() = this.i and 0xF // Bitmask to get low byte
val Byte.hi: Int get() = (this.i and 0xF0) shr 4 // Bitmask and shift to get the high byte
val Byte.hex: String get() = Integer.toHexString(this.i)
val Int.hex: String get() = Integer.toHexString(this)
fun address(msb: Byte, lsb: Byte) = ((msb.toInt() and 0xF) shl 8) or (lsb.toInt() and 0xFF) // Combine msb and lsb into a 12-bit address
