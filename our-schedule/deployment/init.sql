-- Create schedule database and user
CREATE DATABASE schedule;
CREATE USER schedule WITH PASSWORD 'schedule';
GRANT ALL PRIVILEGES ON DATABASE schedule TO schedule;

-- Connect to schedule database to grant schema privileges
\c schedule
GRANT ALL ON SCHEMA public TO schedule;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO schedule;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO schedule;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON TABLES TO schedule;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON SEQUENCES TO schedule;

-- Create keycloak database and user
\c postgres
CREATE DATABASE keycloak;
CREATE USER keycloak WITH PASSWORD 'keycloak';
GRANT ALL PRIVILEGES ON DATABASE keycloak TO keycloak;

-- Connect to keycloak database to grant schema privileges
\c keycloak
GRANT ALL ON SCHEMA public TO keycloak;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO keycloak;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO keycloak;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON TABLES TO keycloak;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON SEQUENCES TO keycloak;
