CREATE TRIGGER history_trigger AFTER UPDATE
on vendor
FOR EACH ROW EXECUTE PROCEDURE vendor_history()