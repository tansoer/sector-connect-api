-- Drop Tables if they exist
DROP TABLE IF EXISTS user_sector;
DROP TABLE IF EXISTS sectors;
DROP TABLE IF EXISTS users;

-- Create Users Table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    agreed_to_terms BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Sectors Table (Self-Referencing)
CREATE TABLE sectors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id BIGINT DEFAULT NULL,
    is_selectable BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES sectors(id) ON DELETE CASCADE
);

-- Create Many-to-Many Relationship Table for User-Sector
CREATE TABLE user_sector (
    user_id BIGINT NOT NULL,
    sector_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, sector_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (sector_id) REFERENCES sectors(id) ON DELETE CASCADE
);

-- Create Indexes for Performance Optimization
CREATE INDEX idx_sectors_parent_id ON sectors (parent_id);
CREATE INDEX idx_user_sector_user_id ON user_sector (user_id);
CREATE INDEX idx_user_sector_sector_id ON user_sector (sector_id);

-- Root Categories (Top-Level)
INSERT INTO sectors (id, name, parent_id, is_selectable)
VALUES (1, 'Manufacturing', NULL, FALSE),
       (2, 'Service', NULL, FALSE),
       (3, 'Other', NULL, FALSE);

-- Manufacturing Subcategories
INSERT INTO sectors (id, name, parent_id, is_selectable)
VALUES (6, 'Food and Beverage', 1, FALSE),
       (12, 'Machinery', 1, FALSE),
       (13, 'Furniture', 1, FALSE),
       (9, 'Plastic and Rubber', 1, FALSE),
       (11, 'Metalworking', 1, FALSE),
       (5, 'Printing', 1, FALSE),
       (7, 'Textile and Clothing', 1, FALSE),
       (8, 'Wood', 1, FALSE);

-- Food and Beverage Subcategories
INSERT INTO sectors (id, name, parent_id, is_selectable)
VALUES (342, 'Bakery & confectionery products', 6, TRUE),
       (43, 'Beverages', 6, TRUE),
       (42, 'Fish & fish products', 6, TRUE),
       (40, 'Meat & meat products', 6, TRUE),
       (39, 'Milk & dairy products', 6, TRUE),
       (437, 'Other (Food)', 6, TRUE),
       (378, 'Sweets & snack food', 6, TRUE);

-- Furniture Subcategories
INSERT INTO sectors (id, name, parent_id, is_selectable)
VALUES (389, 'Bathroom/sauna', 13, TRUE),
       (385, 'Bedroom', 13, TRUE),
       (390, 'Children’s room', 13, TRUE),
       (98, 'Kitchen', 13, TRUE),
       (101, 'Living room', 13, TRUE),
       (392, 'Office', 13, TRUE),
       (394, 'Other (Furniture)', 13, TRUE),
       (341, 'Outdoor', 13, TRUE),
       (99, 'Project furniture', 13, TRUE);

-- Machinery Subcategories
INSERT INTO sectors (id, name, parent_id, is_selectable)
VALUES (97, 'Maritime', 12, FALSE),
       (224, 'Manufacture of machinery', 12, TRUE),
       (93, 'Metal structures', 12, TRUE),
       (508, 'Other (Machinery)', 12, TRUE),
       (227, 'Repair and maintenance service', 12, TRUE);

-- Maritime Subcategories
INSERT INTO sectors (id, name, parent_id, is_selectable)
VALUES (271, 'Aluminium and steel workboats', 97, TRUE),
       (269, 'Boat/Yacht building', 97, TRUE),
       (230, 'Ship repair and conversion', 97, TRUE);

-- Metalworking Subcategories
INSERT INTO sectors (id, name, parent_id, is_selectable)
VALUES (67, 'Construction of metal structures', 11, TRUE),
       (263, 'Houses and buildings', 11, TRUE),
       (267, 'Metal products', 11, TRUE),
       (542, 'Metal works', 11, FALSE);

-- Metal Works Subcategories
INSERT INTO sectors (id, name, parent_id, is_selectable)
VALUES (75, 'CNC-machining', 542, TRUE),
       (62, 'Forgings, Fasteners', 542, TRUE),
       (69, 'Gas, Plasma, Laser cutting', 542, TRUE),
       (66, 'MIG, TIG, Aluminum welding', 542, TRUE);

-- Plastic and Rubber Subcategories
INSERT INTO sectors (id, name, parent_id, is_selectable)
VALUES (54, 'Packaging', 9, TRUE),
       (556, 'Plastic goods', 9, TRUE),
       (559, 'Plastic processing technology', 9, FALSE),
       (560, 'Plastic profiles', 9, TRUE);

-- Plastic Processing Technology Subcategories
INSERT INTO sectors (id, name, parent_id, is_selectable)
VALUES (55, 'Blowing', 559, TRUE),
       (57, 'Moulding', 559, TRUE),
       (53, 'Plastics welding and processing', 559, TRUE);

-- Printing Subcategories
INSERT INTO sectors (id, name, parent_id, is_selectable)
VALUES (148, 'Advertising', 5, TRUE),
       (150, 'Book/Periodicals printing', 5, TRUE),
       (145, 'Labelling and packaging printing', 5, TRUE);

-- Textile and Clothing Subcategories
INSERT INTO sectors (id, name, parent_id, is_selectable)
VALUES (44, 'Clothing', 7, TRUE),
       (45, 'Textile', 7, TRUE);

-- Wood Subcategories
INSERT INTO sectors (id, name, parent_id, is_selectable)
VALUES (337, 'Other (Wood)', 8, TRUE),
       (51, 'Wooden building materials', 8, TRUE),
       (47, 'Wooden houses', 8, TRUE);

-- Service Subcategories
INSERT INTO sectors (id, name, parent_id, is_selectable)
VALUES (25, 'Business services', 2, TRUE),
       (35, 'Engineering', 2, TRUE),
       (28, 'Information Technology and Telecommunications', 2, FALSE),
       (22, 'Tourism', 2, TRUE),
       (141, 'Translation services', 2, TRUE),
       (21, 'Transport and Logistics', 2, FALSE);

-- Information Technology and Telecommunications Subcategories
INSERT INTO sectors (id, name, parent_id, is_selectable)
VALUES (581, 'Data processing, Web portals, E-marketing', 28, TRUE),
       (576, 'Programming, Consultancy', 28, TRUE),
       (121, 'Software, Hardware', 28, TRUE),
       (122, 'Telecommunications', 28, TRUE);

-- Transport and Logistics Subcategories
INSERT INTO sectors (id, name, parent_id, is_selectable)
VALUES (111, 'Air', 21, TRUE),
       (114, 'Rail', 21, TRUE),
       (112, 'Road', 21, TRUE),
       (113, 'Water', 21, TRUE);

-- Other Subcategories
INSERT INTO sectors (id, name, parent_id, is_selectable)
VALUES (37, 'Creative industries', 3, TRUE),
       (29, 'Energy technology', 3, TRUE),
       (33, 'Environment', 3, TRUE);
