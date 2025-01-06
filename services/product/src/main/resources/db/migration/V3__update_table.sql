-- Rename the original column to a temporary one.
ALTER TABLE product RENAME COLUMN available_quantity TO available_quantity_temp;

-- Add a new column with the correct data type.
ALTER TABLE product ADD COLUMN available_quantity INTEGER;

-- Update the new column with the value from the temporary column and floor the number.
UPDATE product SET available_quantity = FLOOR(available_quantity_temp);

-- Drop the temporary column.
ALTER TABLE product DROP COLUMN available_quantity_temp;