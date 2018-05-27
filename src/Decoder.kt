interface Decoder {
    fun sys(address: Int) // 0NNN: Execute machine language subroutine at address NNN
    fun cls() // 00E0: Clear the screen
    fun ret() // 00EE: Return from a subroutine
    fun jp(address: Int) // 1NNN: Jump to address NNN
    fun call(address: Int) // 2NNN: Execute subroutine starting at address NNN
    fun se(register: Int, value: Int) // 3XNN: Skip if VX is equal to NN
    fun sne(register: Int, value: Int) // 4XNN: Skip if VX is not equal to NN
    fun ser(registerX: Int, registerY: Int) // 5XY0: Skip if VX is equal to VY
    fun set(register: Int, value: Int) // 6XNN: Store the number NN in register VX
    fun add(register: Int, value: Int) // 7XNN: Add the value NN to register VX
    fun setr(registerX: Int, registerY: Int) // 8XY0: Store the value of register VY in register Vx
    fun or(registerX: Int, registerY: Int) // 8XY1: Set VX to VX OR VY
    fun and(registerX: Int, registerY: Int) // 8XY2: Set VX tp VX AND VY
    fun xor(registerX: Int, registerY: Int) // 8XY3: Set VX to VX XOR VY
    fun addr(registerX: Int, registerY: Int) // 8XY4: Add the value of register VY to register VX. Set VF to 01 if a carry occurs or 00 if a carry does not occur
    fun sub(registerX: Int, registerY: Int) // 8XY5: Subtract the value of register VY from register VX. Set VF to 00 if a borrow occurs or 01 if a borrow does not occur
    fun shr(registerY: Int) // 8XY6: Store the value of register VY shifted right one bit in register VX. Set VF to the lsb before shift
    fun subt(registerX: Int, registerY: Int) // 8XY7: Set the register VX to the value of VY minus VX. Set VF to 00 if a borrow occurs or 01 if a borrow does not occur
    fun shl(registerY: Int) // 8XYE: Store the value of register VY shifted left one bit in register VX. Set VF to the msb before shift
    fun sner(registerX: Int, registerY: Int) // 9XY0: Skip if VX is not equal to VY
    fun seti(value: Int) // ANNN: Store memore address NNN in register I
    fun jpv0(value: Int) // BNNN: Jump to address NNN + V0
    fun rand(registerX: Int, value: Int) // CXNN: Set VX to a random number with a mask of NN
    fun draw(registerX: Int, registerY: Int, value: Int) // DXYN: Draw a sprite at position VX, VY with N bytes of sprite data starting at I. Set VF 01 if any set pixels are changed to unset, 00 otherwise
    fun skey(registerX: Int) // EX9E: Skip if the key corresponding to the hex value in VX is pressed
    fun snkey(registerX: Int) // EXA1: Skip if the key corresponding to the hex value in VX is not pressed
    fun getdelay(registerX: Int) // FX07: Store the current value of the delay timer in VX
    fun waitkey(registerX: Int) // FX0A: Wait for a keypress and store the result in VX
    fun setdelay(registerX: Int) // FX15: Set the delay timer to the value of register VX
    fun setsound(registerX: Int) // FX18: Set the sound timer to the value of register VX
    fun addi(registerX: Int) // FX1E: Add the value stored in register VX to register VX
    fun spritei(registerX: Int) // FX29: Set i to the memory address of the sprite data corresponding to the hexadecimal digit stored in register VX
    fun bcd(registerX: Int) // FX33: Store the binary-coded decimal equivalent of the value stored in register VX at addresses I, I+1 and I+2
    fun push(registerX: Int) // FX55: Store the values of registers V0 to VX inclusive in memory starting at address I. I is set to I+X+1 after the operation
    fun pop(registerX: Int) // FX65: Fill registers V0 to VX inclusive with the values stored in memory starting at address I. I is set to I + X + 1 after the operation
}