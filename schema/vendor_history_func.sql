CREATE FUNCTION vendor_history()
                RETURNS trigger AS
                $history_trigger$
                BEGIN
                    INSERT INTO vendor_history VALUES (OLD.*, now());
                    RETURN NULL;
                END
                $history_trigger$
                LANGUAGE plpgsql