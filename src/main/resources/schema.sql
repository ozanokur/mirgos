CREATE INDEX IF NOT EXISTS idx_stores_location
ON stores
USING GIST (location);

CREATE INDEX IF NOT EXISTS idx_courier_locations_courier_time
ON courier_locations (courier_id, time);

CREATE UNIQUE INDEX IF NOT EXISTS idx_courier_distances_courier_id
ON courier_distances (courier_id);

CREATE INDEX IF NOT EXISTS idx_entrances_courier_store_event_time
ON entrances (courier_id, store_id, event_time);