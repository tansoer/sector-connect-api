FROM postgres:15

# Set environment variables
ENV POSTGRES_USER=sectorconnect_user
ENV POSTGRES_PASSWORD=sectorconnect_password
ENV POSTGRES_DB=sectorconnect

# Copy initialization SQL scripts
COPY create_database.sql /docker-entrypoint-initdb.d/

# Set file permissions
RUN chmod 644 /docker-entrypoint-initdb.d/create_database.sql

# Start PostgreSQL
CMD ["postgres"]
