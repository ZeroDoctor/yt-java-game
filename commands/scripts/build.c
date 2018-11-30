#include <stdlib.h>
#include <stdio.h>

int main() {
    printf("%s\n", "building...");
    // res:src on linux
    system("cd .. && javac -d bin -sourcepath res;src src/com/zerulus/game/GameLauncher.java");
    printf("%s\n\n", "Done! - building");

    return 0;
}
