USE [DB_9E49A0_kritsin]
GO

/****** Object:  Table [dbo].[notes]    Script Date: 04.12.2015 4:41:08 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[notes](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NOT NULL,
	[note] [nvarchar](255) NULL
) ON [PRIMARY]

GO

ALTER TABLE [dbo].[notes]  WITH NOCHECK ADD  CONSTRAINT [fk_user_id] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([id])
GO

ALTER TABLE [dbo].[notes] CHECK CONSTRAINT [fk_user_id]
GO


