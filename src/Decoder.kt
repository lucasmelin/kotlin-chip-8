interface Decoder {
    fun sys(address: Int) // 0NNN: Execute machine language subroutine at address NNN
    fun cls() // 00E0: Clear the screen
    fun ret() // 00EE: Return from a subroutine
    fun jp() // 1NNN: Jump to address NNN
    fun call(address: Int) // 2NNN: Execute subroutine starting at address NNN
    fun se(register: Int, value: Int) // 3XNN: Skip if Vx is equal to NN
    fun sne(register: Int, value: Int) // 4XNN: Skip if Vx is not equal to NN
    fun ser(registerx: Int, registery: Int) // 5XY0: Skip if Vx is equal to Vy
    fun set(register: Int, value: Int) // 6XNN: Store the number NN in register Vx
}