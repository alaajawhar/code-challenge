FROM postgres:15

ENV POSTGRES_DB=postgres
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=password

COPY .docker/postgres/init.sql /docker-entrypoint-initdb.d/

EXPOSE 5432