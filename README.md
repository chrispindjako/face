# Face

Quickly expose your database information through the API via simple JSON configuration files.

## To start

To get started you will need to know how to use JSON files and know the SQL language...

### Prerequisites

- Java 8
- Sql Server or Oracle Sql or MySql or PostgreSQL

### Installation

_Windows_:
[The steps to install program....](https://docs.microsoft.com/en-us/dotnet/framework/windows-services/how-to-install-and-uninstall-services)

## Structure
```bash
├── config                                   # main configuration folder
│   │── datasource                           # datasource configuration folder
│   │   ├── user-datasource-example.json     # example configuration for datasource 
│   │   └── ...
│   ├── security                             # security configuration folder
│   │   ├── user-security-example.json       # example configuration for security 
│   │   └── ...
│   ├── get-user-data-example.json           # example configuration for API
│   └── ...
└── face.jar

Test with GET localhost:9100/api/get-user-data-example?gender=M
```
## Made with

* [Spring Boot](https://spring.io/projects/spring-boot) - Spring Boot is an open source Java-based framework used to create a micro Service.
* [Spring Tools](https://spring.io/tools) - Spring Tools is the next generation of Spring tooling for your favorite coding environment

## Contributing

If you want to contribute, read the file [CONTRIBUTING.md](https://github.com/chrispindjako/face) to know how to do it.

## Versions

**Last release version :** 1.0
**Last version :** 1.0
Liste des versions : [Click to view](https://github.com/chrispindjako/face/tags)
## Auteurs

* **Chrispin Djako** _alias_ [@chrispindjako](https://github.com/chrispindjako)

Read the list of [contributors](https://github.com/chrispindjako/face/contributors) to see who helped with the project!

## License

This project is licensed ``Apache License 2.0`` - view file [LICENSE](https://github.com/chrispindjako/face/blob/main/LICENSE) for more information

