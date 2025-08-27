-- Database migration for friend functionality and enhanced features
-- StudentHub Application Backend
-- This script can be run manually if needed

-- Create friendships table
CREATE TABLE IF NOT EXISTS friendships (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    from_user_id BIGINT NOT NULL,
    to_user_id BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (from_user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (to_user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_friendship (from_user_id, to_user_id)
);

-- Create channel_memberships table
CREATE TABLE IF NOT EXISTS channel_memberships (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    channel_id BIGINT NOT NULL,
    role VARCHAR(20) DEFAULT 'MEMBER',
    is_active BOOLEAN DEFAULT TRUE,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    left_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (channel_id) REFERENCES channel(channel_id) ON DELETE CASCADE,
    UNIQUE KEY unique_membership (user_id, channel_id)
);

-- Add columns to users table if they don't exist
ALTER TABLE users 
ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'OFFLINE',
ADD COLUMN IF NOT EXISTS last_seen TIMESTAMP NULL,
ADD COLUMN IF NOT EXISTS avatar VARCHAR(500) NULL,
ADD COLUMN IF NOT EXISTS is_online BOOLEAN DEFAULT FALSE,
ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- Add columns to channel table if they don't exist
ALTER TABLE channel 
ADD COLUMN IF NOT EXISTS is_private BOOLEAN DEFAULT FALSE,
ADD COLUMN IF NOT EXISTS created_by BIGINT NULL,
ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- Add foreign key for created_by if it doesn't exist
ALTER TABLE channel 
ADD CONSTRAINT IF NOT EXISTS fk_channel_created_by 
FOREIGN KEY (created_by) REFERENCES users(user_id);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_users_online ON users(is_online);
CREATE INDEX IF NOT EXISTS idx_users_status ON users(status);
CREATE INDEX IF NOT EXISTS idx_users_last_seen ON users(last_seen);

CREATE INDEX IF NOT EXISTS idx_friendships_from_user ON friendships(from_user_id);
CREATE INDEX IF NOT EXISTS idx_friendships_to_user ON friendships(to_user_id);
CREATE INDEX IF NOT EXISTS idx_friendships_status ON friendships(status);

CREATE INDEX IF NOT EXISTS idx_memberships_user ON channel_memberships(user_id);
CREATE INDEX IF NOT EXISTS idx_memberships_channel ON channel_memberships(channel_id);
CREATE INDEX IF NOT EXISTS idx_memberships_active ON channel_memberships(is_active);
CREATE INDEX IF NOT EXISTS idx_memberships_role ON channel_memberships(role);

-- Update existing users to have default avatar if null
UPDATE users 
SET avatar = CONCAT('https://ui-avatars.com/api/?name=', 
                   COALESCE(user_first_name, 'User'), 
                   '&background=7289da&color=fff')
WHERE avatar IS NULL;

-- Migrate existing channel memberships if channel_members table exists
INSERT IGNORE INTO channel_memberships (user_id, channel_id, role, is_active, joined_at)
SELECT cm.user_id, cm.channel_id, 'MEMBER' as role, TRUE as is_active, NOW() as joined_at
FROM channel_members cm
WHERE EXISTS (SELECT 1 FROM information_schema.tables 
              WHERE table_schema = DATABASE() 
              AND table_name = 'channel_members');

COMMIT;

-- Output success message
SELECT 'Friend functionality and enhanced features migration completed successfully!' as Status;
