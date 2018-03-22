比特币钱包
#编译jar包
gradle build -x test

#查看包依赖
gradlew dependencies

#运行jar包，可以设置启动参数
java -jar -server -Xms256M -Xmx256M -Xss256k chain-bitcoinj-1.0.0-SNAPSHOT.jar
