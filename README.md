# Spring Boot learning

## Environment & installation
- MacOS / Homebrew
- Docker
- JDK (8+):
  - Run `brew install openjdk`
  - Link JDK: `sudo ln -sfn /opt/homebrew/opt/openjdk/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk.jdk`
  - Add to path:
    ```bash
    #~/.zshrc
    export PATH="/opt/homebrew/opt/openjdk/bin:$PATH"
    export JAVA_HOME=$(/usr/libexec/java_home)
    export PATH="$JAVA_HOME/bin:$PATH"
    ```

  - Run: `source ~/.zshrc`
  - Check: `java -version`, `/usr/libexec/java_home --version`

- Maven: `brew install maven`
  - Check: `mvn -v`
- IDE: VSCode
  - Extensions:
    - Extension Pack for Java
    - Maven for Java
    - Spring Boot Extension Pack 

## Project setup
- Build: `mvn clean install`