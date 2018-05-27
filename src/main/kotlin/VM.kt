data class VM (
        /* 4kb of 8-bit memory
         Fontset begins at 0x50
         Program begins at 0x200
         */
        val memory: ByteArray = ByteArray(4096), // RAM + ROM
        /* Subroutine callstack which allows for 16 levels of nesting
        Used to store return addresses */
        val stack: IntArray = IntArray(16), //
        /* 16 8-bit registers
        Used for storing data. Register 0xF is used to detect
        Carry, Borrow and Collisions
         */
        val registers: ByteArray = ByteArray(16),
        /* Contains the keyboard state
         */
        val keys: ByteArray = ByteArray(16),
        /* 64 x 32 pixel monochrome display
         */
        val display: ByteArray = ByteArray(2048),
        /* 16-bit address register, only the first 12 bits are used
        Points to a specific location in memory
         */
        val I: Int = 0,
        /* 16-bit program counter, only the first 12 bits are used
        Points to the current operation. Starts at 0x200
         */
        var pc: Int = 0x200,
        /* Points to the next free slot in the stack
         */
        var sc: Int = 0,
        /* Used to delay events
         */
        val delay: Int = 0,
        /* Used to delay sounds
         */
        val sound: Int = 0, // Sound timer
        val romSize: Int = 0 // Size of the loaded rom
)