import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.experimental.xor

class Interpreter(val state: VM) : Decoder {

    fun step() {
        val msb = state.memory[state.pc]
        val lsb = state.memory[state.pc + 1]
        decode(state.pc, msb, lsb)
    }


    override fun sys(address: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Clear the screen. Fills the display array with zeroes.
     */
    override fun cls() {
        // Fill the display array with zeroes
        Arrays.fill(state.display, 0)
    }

    /**
     * Return from a subroutine.
     */
    override fun ret() {
        state.pc = state.stack[state.sc]
        state.sc--
    }

    /**
     * Jump to a specific memory address.
     */
    override fun jp(address: Int) {
        state.pc = address
    }

    /**
     * Execute subroutine starting at address.
     */
    override fun call(address: Int) {
        state.sc++
        state.stack[state.sc] = state.pc
        state.pc = address
    }

    /**
     * Skip if register is equal to value.
     */
    override fun se(register: Int, value: Int) {
        if (state.registers[register] == value.toByte()) {
            state.pc += 2
        }
        state.pc += 2
    }

    /**
     * Skip if register is not equal to value.
     */
    override fun sne(register: Int, value: Int) {
        if (state.registers[register] != value.toByte()) {
            state.pc += 2
        }
        state.pc += 2
    }

    /**
     * Skip if registers are equal.
     */
    override fun ser(registerX: Int, registerY: Int) {
        if (state.registers[registerX] == state.registers[registerY]) {
            state.pc += 2
        }
        state.pc += 2
    }

    /**
     * Store the value in the register.
     */
    override fun ld(register: Int, value: Int) {
        state.registers[register] = value.toByte()
        state.pc += 2
    }

    /**
     * Add the value to the register value.
     */
    override fun add(register: Int, value: Int) {
        state.registers[register] = (state.registers[register] + value.toByte()).toByte()
        state.pc += 2
    }

    /**
     * Store the value of registerY in registerX.
     */
    override fun setr(registerX: Int, registerY: Int) {
        state.registers[registerX] = state.registers[registerY]
        state.pc += 2
    }

    override fun or(registerX: Int, registerY: Int) {
        state.registers[registerX] = state.registers[registerX].or(state.registers[registerY])
        state.pc += 2
    }

    override fun and(registerX: Int, registerY: Int) {
        state.registers[registerX] = state.registers[registerX].and(state.registers[registerY])
        state.pc += 2
    }

    override fun xor(registerX: Int, registerY: Int) {
        state.registers[registerX] = state.registers[registerX].xor(state.registers[registerY])
        state.pc += 2
    }

    override fun addr(registerX: Int, registerY: Int) {
        val temp = state.registers[registerX] + state.registers[registerY]
        state.registers[registerX] = temp.toByte()
        state.registers[0xF] = if (temp > 0xff) 1 else 0
        state.pc += 2
    }

    override fun sub(registerX: Int, registerY: Int) {
        val regX = state.registers[registerX]
        val regY = state.registers[registerY]
        state.registers[registerX] = (regX - regY).toByte()
        state.registers[0xF] = if (regX > regY) 1 else 0
        state.pc += 2
    }

    override fun shr(registerY: Int) {
        state.registers[0xF] = state.registers[registerY].and(0x1)
        state.registers[registerY] = state.registers[registerY].toInt().shr(1).toByte()
        state.pc += 2
    }

    override fun subn(registerX: Int, registerY: Int) {
        val regX = state.registers[registerX]
        val regY = state.registers[registerY]
        state.registers[registerX] = (regY - regX).toByte()
        state.registers[0xF] = if (regY > regX) 1 else 0
        state.pc += 2
    }

    override fun shl(registerY: Int) {
        state.registers[0xF] = if (state.registers[registerY].toInt().and(0x80) != 0) 1 else 0
        state.registers[registerY] = state.registers[registerY].toInt().shl(1).toByte()
        state.pc += 2
    }

    override fun sner(registerX: Int, registerY: Int) {
        if (state.registers[registerX] != state.registers[registerY]) {
            state.pc += 2
        }
        state.pc += 2
    }

    override fun ldi(value: Int) {
        state.I = value
        state.pc += 2
    }

    override fun jpv0(value: Int) {
        state.pc = value + state.pc
    }

    override fun rnd(registerX: Int, value: Int) {
        state.registers[registerX] = Random().nextInt(0xFF + 1).toByte()
        state.pc += 2
    }

    override fun drw(registerX: Int, registerY: Int, value: Int) {
        val coordX = state.registers[registerX].toInt().toByte()
        val coordY = state.registers[registerY].toInt().toByte()
        val j = value.toByte()

        state.registers[0xF] = 0

        for (y in 0 until j) {
            val row = state.memory[state.I + y]
            for (x in 0..7) {
                val pixel = if (row.toInt() and (1.shl(7 - x)) != 0) 1 else 0
                if (pixel != 0) {
                    val addr = coordX + x + (coordY + y) * 64
                    if (addr >= 64 * 32) continue
                    val oldPixel = state.display[addr]
                    val newPixel = oldPixel.toInt().xor(pixel)
                    state.display[addr] = newPixel.and(0x1).toByte()
                    if (oldPixel.toInt() == 1) {
                        state.registers[0xF] = 1
                    }
                }
            }
        }
        state.pc += 2
    }

    override fun skp(registerX: Int) {
        if (state.keys[state.registers[registerX].toInt()].toInt() == 0) {
            state.pc += 2
        }
        state.pc += 2
    }

    override fun sknp(registerX: Int) {
        if (state.keys[state.registers[registerX].toInt()].toInt() != 0) {
            state.pc += 2
        }
        state.pc += 2
    }

    override fun getdelay(registerX: Int) {
        state.registers[registerX] = state.delay.toByte()
        state.pc += 2
    }

    override fun waitkey(registerX: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setdelay(registerX: Int) {
        state.delay = state.registers[registerX].toInt()
        state.pc += 2
    }

    override fun setsound(registerX: Int) {
        state.sound = state.registers[registerX].toInt()
    }

    override fun addi(registerX: Int) {
        state.I = (state.I + state.registers[registerX])
        state.pc += 2
    }

    override fun spritei(registerX: Int) {
        val idx = state.registers[registerX] * 5
        if (idx >= 16 * 5) {
            state.I = 0
        } else {
            state.I = idx
        }
        state.pc += 2
    }

    override fun bcd(registerX: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun push(registerX: Int) {
        var addr = state.I
        for (i in 0..registerX) {
            state.memory[addr] = state.registers[i]
            addr += 1
        }
        state.pc += 2
    }

    override fun pop(registerX: Int) {
        var addr = state.I
        for (i in 0..registerX) {
            state.registers[i] = state.memory[addr]
            addr += 1
        }
        state.pc += 2
    }

    override fun unknown(opcode: Int, address: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}