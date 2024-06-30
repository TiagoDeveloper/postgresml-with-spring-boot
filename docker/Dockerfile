FROM ghcr.io/postgresml/postgresml:2.7.13

COPY entrypoint.sh /app/entrypoint.sh
COPY dashboard.sh /app/dashboard.sh

EXPOSE 8000 5432

ENTRYPOINT ["bash", "/app/entrypoint.sh", "bash", "/app/dashboard.sh" ]