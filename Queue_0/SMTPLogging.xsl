<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" indent="yes" />

<!-- variables -->
<xsl:variable name='Table'>font-family:Tahoma; font-size:75%; text-align:left; vertical-align:top; table-layout:variable</xsl:variable>
<xsl:variable name='Gutter'>width:1ex</xsl:variable>
<xsl:variable name='Header'>border-bottom:1 solid black</xsl:variable>

<xsl:template match="SMTPLOGINFO">

<html>
    <head>
        <title>NeoSMTP version 2.5.1 에러 메시지 정보 - Neocast Co.,Ltd</title>
    </head>

    <body style='margin:10'>
        <table id='BodyTable' style="{$Table}" cellspacing='1' border='0'>

            <col style="width:25ex;"/>
            <col style='{$Gutter}' />
            <col style="width:30ex;"/>
            <col style='{$Gutter}' />
            <col style="width:15ex;"/>
            <col style='{$Gutter}' />
            <col style="width:15ex;"/>
            <col style='{$Gutter}' />
            <col style="width:20ex;"/>
            <col style='{$Gutter}' />
            <col style="width:45ex;"/>
            <col style='{$Gutter}' />
            <col style="width:70ex;"/>
            <col style='{$Gutter}' />

			<thead>
                <tr>
                    <th style="{$Header}">메일아이디</th>
                    <th/>
                    <th style="{$Header}">이메일</th>
                    <th/>
                    <th style="{$Header}">이름</th>
                    <th/>
                    <th style="{$Header}">아이디</th>
                    <th/>
                    <th style="{$Header}">발생시간</th>
                    <th/>
                    <th style="{$Header}">진행단계</th>
                    <th/>
                    <th style="{$Header}">SMTP메시지</th>
                </tr>
            </thead>

            <tbody style='vertical-align:top'>
				<xsl:apply-templates select="MAILINFO">
					<xsl:sort select="MSGID" />
					<xsl:sort select="LOGTIME" />
					<xsl:sort select="EMAIL" />
					<xsl:sort select="NAME" />
				</xsl:apply-templates>
            </tbody>
        </table>
    </body>
    </html>
</xsl:template>

<xsl:template match="MAILINFO">
	<tr>
		<td> <xsl:value-of select="MSGID"/> </td>
		<td />
		<td> <xsl:value-of select="EMAIL"/> </td>
		<td />
		<td> <xsl:value-of select="NAME"/> </td>
		<td />
		<td> <xsl:value-of select="USERID"/> </td>
		<td />
		<td align="center"> <xsl:value-of select="LOGTIME"/> </td>
		<td />
		<td> <xsl:value-of select="PROCEED"/> </td>
		<td />
		<td> <xsl:value-of select="SMTPMSG"/> </td>
	</tr>
	<tr />
	<tr height="1" bgcolor="#C0C0C0">
		<td colspan="13"></td>
	</tr>
</xsl:template>

</xsl:stylesheet>
