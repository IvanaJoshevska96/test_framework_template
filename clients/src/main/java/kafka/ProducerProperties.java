package kafka;

public enum ProducerProperties {
    BOOTSTRAP_SERVERS("bootstrap.servers"),
    SECURITY_PROTOCOL("security.protocol"),
    SASL_MECHANISM("sasl.mechanism"),
    SASL_JAAS_CONFIG("sasl.jaas.config"),
    TRUSTSTORE_LOCATION("ssl.truststore.location"),
    TRUSTSTORE_PASSWORD("ssl.truststore.password"),
    KEYSTORE_LOCATION("ssl.keystore.location"),
    KEYSTORE_PASSWORD("ssl.keystore.password"),
    KEY_PASSWORD("ssl.key.password"),

    ACKS("acks"),
    RETRIES("retries"),
    BATCH_SIZE("batch.size"),
    LINGER_MS("linger.ms"),
    BUFFER_MEMORY("buffer.memory"),

    KEY_SERIALIZER("key.serializer"),
    VALUE_SERIALIZER("value.serializer");

    ProducerProperties(final String property) {
        this.property = property;
    }

    private final String property;

    public String getProperty() {
        return property;
    }
}
