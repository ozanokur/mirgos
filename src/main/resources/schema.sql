CREATE INDEX IF NOT EXISTS idx_stores_location
ON stores
USING GIST (location);