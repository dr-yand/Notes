USE [DB_9E49A0_kritsin]
GO

/****** Object:  Table [dbo].[users]    Script Date: 04.12.2015 4:28:28 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[users](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[login] [nvarchar](100) NOT NULL,
	[password] [nvarchar](100) NOT NULL,
	[name] [nvarchar](100) NOT NULL
) ON [PRIMARY]

GO


