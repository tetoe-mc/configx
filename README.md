# Config X

[![Author](https://img.shields.io/badge/Author-NriotHrreion-red.svg "Author")](https://github.com/NriotHrreion)
[![LICENSE](https://img.shields.io/badge/License-CC0_1.0-green.svg "LICENSE")](./LICENSE)
[![Stars](https://img.shields.io/github/stars/tetoe-mc/configx.svg?label=Stars&style=flat)](https://github.com/tetoe-mc/configx/stargazers)
[![Github Workflow Status](https://img.shields.io/github/actions/workflow/status/tetoe-mc/configx/build.yml)](https://github.com/tetoe-mc/configx/actions/workflows/build.yml)

## Description

This is a fabric API library for plugins to load & save configurations.

## Installation

1. Add the following to your build script:

```gradle
repositories {
    maven {
        url = uri("https://repo.codemc.io/repository/tetoe-mc")
    }
}

dependencies {
    // Approach #1: Ensure Config X is always available by including it within your own jar
    include(modImplementation("space.nocp:configx:2.0.1"))
    
    // Approach #2: Depend on Config X, but require that users install it themselves
    modImplementation "space.nocp:configx:2.0.1"
}
```

2. Refresh your project.

## Usage

Remember to import the package before starting.

```java
import space.nocp.configx.api.*;
```

#### Register a configuration

Before registering a configuration, you need to write a class describing the configuration object and prepare a default configuration. Here, let's call the class `ConfigType`.

```java
// ...
Configuration config = ConfigManager.get().register("my-config", defaultConfig, ConfigType.class);
```

#### Get a configuration

After registering your configuration, you can use `config()` method to get the configuration at anywhere in your project.

```java
Configuration config = ConfigManager.get().config("my-config");
```

#### Get & set the configuration object

```java
Configuration config = ...;
ConfigType configObj = config.get();

configObj.myValue = "Hello World";
config.set(configObj);
config.save(); // Remember to save the config after setting it
```

#### Load the configuration from the file system manually

Usually, the configuration is always up-to-date with the one storing in the file system. But sometimes, you may want to refresh it manually.

```java
Configuration config = ...;
config.load();
```

#### Get the info of the configuration

```java
Configuration config = ...;

String id = config.id; // my-config
config.getFileName(); // my-config.json
config.getPath().toString(); // C:/path/to/config/my-config.json
```

#### Use custom Gson to parse JSON

Config X stores your configuration in the JSON format. When you stores objects that requires custom serializers and deserializers, you need to build your own Gson to achieve this.

Before registering your configuration, you need to prepare your Gson in advance. Here, let's assume we already have a custom Gson object called `myGson`.

Just simply add the Gson object as a parameter after all the parameters in `register()` method, and the parsing operations will be done with the provided Gson.

```java
// ...
Configuration config = ConfigManager.get().register("my-config", defaultConfig, ConfigType.class, myGson);
```

For more information about custom serializer and deserializer in Gson, see the article: [Building a Personalized Serializer and Deserializer using Java Gson Library](https://medium.com/@alexandre.therrien3/personalized-serializer-and-deserializer-using-java-gson-library-c079de3974d4)

## Build from source

```cmd
git clone https://github.com/tetoe-mc/configx.git
cd configx
./gradlew build
```

Then you can get the jar file in the build folder.

## LICENSE

[CC0 1.0](./LICENSE)
