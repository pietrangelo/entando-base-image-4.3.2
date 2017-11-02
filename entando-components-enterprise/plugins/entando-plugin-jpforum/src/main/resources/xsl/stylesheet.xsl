<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		omit-xml-declaration="yes" />
	<xsl:preserve-space elements="br" />

	<xsl:template match="b">
		<strong>
			<xsl:apply-templates />
		</strong>
	</xsl:template>

	<xsl:template match="i">
		<em>
			<xsl:apply-templates />
		</em>
	</xsl:template>

	<xsl:template match="u">
		<span style="text-decoration: underline;">
			<xsl:apply-templates />
		</span>
	</xsl:template>

	<xsl:template match="a">
		<a href="{.}">
			<xsl:value-of select="@alias" />
		</a>
	</xsl:template>

	<!-- 
	<xsl:template match="url">
		<a href="{.}">
			<xsl:value-of select="." />
		</a>
	</xsl:template>
	 -->

	<xsl:template match="url">
		<a href="{.}">
			<xsl:value-of select="@alias" />
		</a>
	</xsl:template>

	<xsl:template match="color">
		<span style="color:{@color}">
			<xsl:apply-templates />
		</span>
	</xsl:template>

	<xsl:template match="size">
		<span style="font-size:{@size}px">
			<xsl:apply-templates />
		</span>
	</xsl:template>

	<xsl:template match="code">
		<pre>
			<xsl:apply-templates />
		</pre>
	</xsl:template>

	<xsl:template match="quote">
		<blockquote>
			<div class="citazione">
				<xsl:apply-templates />
			</div>
		</blockquote>
	</xsl:template>

	<xsl:template match="img">
		<img src="{.}" alt="immagine" />
	</xsl:template>


	<xsl:template match="title1">
			<span style="font-size: 2.5em; display: block; margin: 1em 1em 1em 0; font-weight: bold;"><xsl:apply-templates /></span>
	</xsl:template>

	<xsl:template match="title2">
			<span style="font-size: 2em; display: block; font-weight: bold; margin: 1em 1em 1em 0; font-style: italic;"><xsl:apply-templates /></span>
	</xsl:template>

	<xsl:template match="title3">
			<span style="font-size: 1.75em; display: block; margin: 1em 1em 1em 0"><xsl:apply-templates /></span>
	</xsl:template>

	<xsl:template match="ol">
		<ol>
			<xsl:apply-templates />
		</ol>
	</xsl:template>


	<xsl:template match="ul">
		<ul>
			<xsl:apply-templates />
		</ul>
	</xsl:template>


	<xsl:template match="li">
		<li>
			<xsl:apply-templates />
		</li>
	</xsl:template>


	<xsl:template match="br">
		<br class="TODO" />
	</xsl:template>

	<xsl:template match="c">
		<blockquote>
			<xsl:apply-templates />
		</blockquote>
	</xsl:template>

	<xsl:template match="c1">
		<div class="c1">
			<xsl:apply-templates />
		</div>
	</xsl:template>

	<xsl:template match="c2">
		<div class="c2">
			<xsl:apply-templates />
		</div>
	</xsl:template>

	<xsl:template match="c3">
		<div class="c3">
			<xsl:apply-templates />
		</div>
	</xsl:template>

	<xsl:template match="c4">
		<div class="c4">
			<xsl:apply-templates />
		</div>
	</xsl:template>

	<xsl:template match="row">
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="citato">
		<div class="citazione"><xsl:apply-templates /><br class="TODO" /></div>
	</xsl:template>


</xsl:stylesheet>