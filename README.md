# Spring Kafka + Circuit Breaker

Exemplo de consumer com Spring Kafka que persiste os registros consumidos
em uma base de dados relacional e os envia para um endpoint REST.

Através do circuit breaker, quando aberto, pausa o consumer. Reativando-o
novamente quando o circuito fechar.

E mesmo com o circuito aberto o consumidor que persiste os dados continuará
seu trabalho, ou seja, eles são independentes.

## Requerimentos

- JDK 1.8
- Acesso ao repositório https://repo.maven.apache.org/maven2/ ou uma 
alternativa com acesso às dependências presentes no `pom.xml`

## Build & Run

### Maven

Para montar o fatjar, execute o comando:

__Linux__

```bash
./mvnw clean package
```

__Windows__

```powershell
.\mvnw.cmd clean package
```

#### Executar

> Você pode utilizar o [`docker-compose.yaml`](./docker-compose.yaml) para
subir um Kafka em sua máquina

Para simular uma API que será utilizada para receber os dados consumidos
do Kafka existe o diretório [some-api](./some-api). Nele há uma configuração
para executar o wiremock através do comando:

```console
java -jar wiremock-standalone-2.26.3.jar \
  --port 38080 \
  --verbose
```

__Linux__

```console
java \
  -Dapp.some.http.endpoint.url='http://localhost:38080' \
  -Dspring.kafka.bootstrap-servers='localhost:9092' \
  -Dspring.kafka.consumer.client-id='spring-kafka-ex' \
  -Dspring.kafka.consumer.group-id='meu-grupo' \
  -jar target/app-spring-boot.jar
```

> Se você parar o wiremock o circuíto abrirá, pausando o consumo. E iniciando-o
novamente, o Kafka Consumer será retomado.

__Simular erro 400 no endpoint__
```console
java \
  -Dapp.some.http.endpoint.url='http://localhost:38080' \
  -Dapp.some.uri='/some/v2' \
  -Dspring.kafka.bootstrap-servers='localhost:9092' \
  -Dspring.kafka.consumer.client-id='spring-kafka-ex' \
  -Dspring.kafka.consumer.group-id='meu-grupo' \
  -jar target/app-spring-boot.jar
```
__Windows__

#### Produza registros

```bash
kafka-producer-perf-test.sh \
  --topic topico \
  --num-records 1000 \
  --record-size 50 \
  --throughput 1 \
  --producer-props \
      acks=1 \
      bootstrap.servers=localhost:9092
```

> Serão produzidos 1000 registros, com 50 bytes cada e na taxa de 1 registro
por segundo

### Docker

A definição [Dockerfile](./Dockerfile) desta aplicação emprega 
[multi-stage builds](https://docs.docker.com/develop/develop-images/multistage-build/).
Isso significa que nela acontece o build da aplicação e a criação da imagem.

Se for necessário somente a criar a imagem, pode-se utilizar a definição 
[Dockerfile-image](./Dockerfile-image). Mas antes é necessário montar
o fatjar através do maven.

Para build do fatjar e montar a imagem, execute o comando:

```bash
docker build . -t cb-kafka-ex:1.0
```

Para montar apenas a imagem (antes é necessário o build do maven):

```bash
docker build -f Dockerfile-image . -t cb-kafka-ex:1.0
```

Para rodar o container:

> Utilize o [docker-compose](./docker-compose.yaml) e inicie todos os serviços
para testes

```bash
docker run -p 8080:8080 \
       -i --rm \
       sk-consumer-ex:1.0
```

## Cobertura

- Executar os testes

```bash
mvn clean test
```

- Acessar o relatório: `target/site/jacoco/index.html`

## Caminho HTTP p/ Verificações de Saúde

> Health Checks

Essas são configurações comuns quando a implantação do aplicação 
é feita em container, especificamente no Kubernetes.

- [Liveness Probe](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/#define-a-liveness-http-request)
  - `/actuator/info`

- [Readiness Probe](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/#define-readiness-probes)
  - `/actuator/health`

## Dicas

### Docker Compose

Neste repositório existe o arquivo [docker-compose.yaml](./docker-compose.yaml),
que inicia todas as partes móveis que a aplicação depende.

Iniciar a stack:

```bash
docker-compose up
```

Serviços presentes na stack

- Kafka: `localhost:9092`
