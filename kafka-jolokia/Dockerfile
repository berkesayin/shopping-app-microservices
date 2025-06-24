FROM confluentinc/cp-kafka:latest
ARG JOLOKIA_AGENT_PATH="/usr/share/java/jolokia-agent.jar"
USER root
COPY jolokia-agent-jvm-2.2.9-javaagent.jar ${JOLOKIA_AGENT_PATH}

# change ownership of copied file to 'appuser', so Kafka can read it
RUN chown appuser:appuser "${JOLOKIA_AGENT_PATH}"

USER appuser

# set KAFKA_OPTS environment variable to use the agent
ENV KAFKA_OPTS="-javaagent:${JOLOKIA_AGENT_PATH}=port=8778,host=0.0.0.0"