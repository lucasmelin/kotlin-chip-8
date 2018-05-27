interface Decoder {
    fun call(address: Int) // 0NNN: Execute machine language subroutine at address NNN
    fun clear() // 00E0: Clear the screen
    fun ret() // 00EE: Return from a subroutine
    fun jump() // 1NNN: Jump to address NNN
}