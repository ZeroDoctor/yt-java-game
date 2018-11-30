#include <stdlib.h>
#include <stdio.h>

int main() {
    printf("%s\n", "running...");
    // bin:res on linux
    system("cd .. && java -cp bin;res com.zerulus.game.GameLauncher");
    printf("%s\n\n", "Done! - running");

    return 0;
}
