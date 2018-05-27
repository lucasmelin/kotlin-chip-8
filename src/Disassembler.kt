class Disassembler() : Decoder {

    private val sb = StringBuilder()

    override fun toString(): String {
        return sb.toString()
    }

    override fun unknown(opcode: Int, address: Int){
        sb.append("unknown op:0x${opcode.hex} 0x${address.hex}")
    }

    override fun sys(address: Int) {
        sb.append("sys 0x${address.hex}")
    }

    override fun cls() {
        sb.appendln("cls")
    }

    override fun ret() {
        sb.appendln("ret")
        //vm.pc = vm.stack[--vm.sc]
    }

    override fun jp(address: Int) {
        sb.appendln("jp 0x${address.hex}")
    }

    override fun call(address: Int) {
        sb.appendln("call 0x${address.hex}")
    }

    override fun se(register: Int, value: Int) {
        sb.appendln("se v${register.hex} 0x${value.hex}")
    }

    override fun sne(register: Int, value: Int) {
        sb.appendln("sne v${register.hex} 0x${value.hex}")
    }

    override fun ser(registerX: Int, registerY: Int) {
        sb.appendln("ser v${registerX.hex} v${registerY.hex}")
    }

    override fun ld(register: Int, value: Int) {
        sb.appendln("ld v${register.hex} 0x${value.hex}")
    }

    override fun add(register: Int, value: Int) {
        sb.appendln("sub v${register.hex} 0x${value.hex}")
    }

    override fun setr(registerX: Int, registerY: Int) {
        sb.appendln("setr v${registerX.hex} v${registerY.hex}")
    }

    override fun or(registerX: Int, registerY: Int) {
        sb.appendln("or v${registerX.hex} v${registerY.hex}")
    }

    override fun and(registerX: Int, registerY: Int) {
        sb.appendln("and v${registerX.hex} v${registerY.hex}")
    }

    override fun xor(registerX: Int, registerY: Int) {
        sb.appendln("xor v${registerX.hex} v${registerY.hex}")
    }

    override fun addr(registerX: Int, registerY: Int) {
        sb.appendln("addr v${registerX.hex} v${registerY.hex}")
    }

    override fun sub(registerX: Int, registerY: Int) {
        sb.appendln("sub v${registerX.hex} v${registerY.hex}")
    }

    override fun shr(registerY: Int) {
        sb.appendln("shr v${registerY.hex}")
    }

    override fun subn(registerX: Int, registerY: Int) {
        sb.appendln("subn v${registerX.hex} v${registerY.hex}")
    }

    override fun shl(registerY: Int) {
        sb.appendln("shl v${registerY.hex}")
    }

    override fun sner(registerX: Int, registerY: Int) {
        sb.appendln("sner v${registerX.hex} v${registerY.hex}")
    }

    override fun ldi(value: Int) {
        sb.appendln("ldi 0x${value.hex}")
    }

    override fun jpv0(value: Int) {
        sb.appendln("jpv0 0x${value.hex}")
    }

    override fun rnd(registerX: Int, value: Int) {
        sb.appendln("rnd v${registerX.hex} 0x${value.hex}")
    }

    override fun drw(registerX: Int, registerY: Int, value: Int) {
        sb.appendln("drw v${registerX.hex} v${registerY.hex} 0x${value.hex}")
    }

    override fun skp(registerX: Int) {
        sb.appendln("skp v${registerX.hex}")
    }

    override fun sknp(registerX: Int) {
        sb.appendln("sknp v${registerX.hex}")
    }

    override fun getdelay(registerX: Int) {
        sb.appendln("getdelay v${registerX.hex}")
    }

    override fun waitkey(registerX: Int) {
        sb.appendln("waitkey v${registerX.hex}")
    }

    override fun setdelay(registerX: Int) {
        sb.appendln("setdelay v${registerX.hex}")
    }

    override fun setsound(registerX: Int) {
        sb.appendln("setsound v${registerX.hex}")
    }

    override fun addi(registerX: Int) {
        sb.appendln("addi v${registerX.hex}")
    }

    override fun spritei(registerX: Int) {
        sb.appendln("spritei v${registerX.hex}")
    }

    override fun bcd(registerX: Int) {
        sb.appendln("bcd v${registerX.hex}")
    }

    override fun push(registerX: Int) {
        sb.appendln("push v0-v${registerX.hex}")
    }

    override fun pop(registerX: Int) {
        sb.appendln("pop v0-v${registerX.hex}")
    }


}