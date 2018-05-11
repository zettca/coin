# Coin

Coin: Cryptocurrency created for SEC project

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You will need:
 * `maven` and Java `jdk8` to build/compile and run the project
 * Apache jUDDI for service discovery ([mirror](https://bit.ly/2wxswTa))

```
sudo apt install maven openjdk-8-jdk
```

### Installing

1. Clone the repository `git clone git@github.com:zettca/coin.git` 
2. Make sure you started jUUDI
3. Run the server `cd server && mvn compile exec:java`
4. Install with `mvn install`


## Running the tests

```
mvn test
```

## Deployment

(No deployment information)

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [IntelliJ IDEA](https://www.jetbrains.com/idea/) - Java IDE

## Authors

* **Bruno Henriques** - [zettca](https://github.com/zettca)
* **Daniel Leitão** - [nucleardannyd](https://github.com/nucleardannyd)
* **Ricardo Campos** - [lolstorm92](https://github.com/lolstorm92)

See also the list of [contributors](https://github.com/zettca/coin/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Coffee is fuel
