
AdvancedCppIDE :
A sleek, lightweight Java-based IDE for C and C++ programming built using Swing.
It supports multi-tab editing, syntax-friendly dark mode, and one-click Compile & Run functionality using gcc or g++ compilers.


✨ Features :

📝 Multi-tab editor with monospaced font

🧠 Dark theme with green-on-black retro coding aesthetic

💾 File operations: New, Open, Save

🧪 One-click Compile & Run (using gcc or g++)

🖥️ Output console embedded at the bottom

🧭 Smart file tracking and auto-tab naming


📦 Requirements:
Java JDK 8+
GCC/G++ (MinGW on Windows recommended)
Windows OS (for cmd /k usage in compilation)


🚀 How to Run:
Compile the project
Open terminal/command prompt and run:
javac AdvancedCppIDE.java
Run the IDE
java AdvancedCppIDE
Use the IDE
Use the File menu to create/open/save .c or .cpp files.
Press Compile & Run to build and execute your C/C++ code.



🧠 How Compilation Works
On clicking Compile & Run:

If the file is .cpp → compiled using g++
If the file is .c → compiled using gcc
Executable named output.exe is generated in the same directory
Output or errors are displayed in the built-in output console
⚠️ Ensure gcc or g++ is added to your system's PATH.


🎨 Dark Theme Customizations :
Tabs, editor, buttons, and menus use custom dark-mode color schemes
Green text and cursor replicate a terminal-style retro look
Custom TabbedPaneUI overrides the default tab rendering


📌 Limitations :
Currently supports only Windows OS for the cmd.exe execution
Does not support syntax highlighting
No error navigation or debugging tools (basic execution only)

📜 License :
This project is open-source and free to use for educational or personal purposes.


🙌 Acknowledgements:
Built with ❤️ in Java Swing.
Inspired by classic terminal IDEs and the simplicity of one-click compilers.

