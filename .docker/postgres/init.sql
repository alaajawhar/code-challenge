CREATE SCHEMA IF NOT EXISTS card_management;
CREATE SCHEMA IF NOT EXISTS fraud_detection;

-- Set default search path to include both schemas
ALTER DATABASE postgres SET search_path TO card_management, fraud_detection, public; 