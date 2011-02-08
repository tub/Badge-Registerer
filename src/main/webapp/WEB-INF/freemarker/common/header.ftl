<!doctype html>
<html>
<head>
<link href="style.css" type="text/css" rel="stylesheet"/>
<title>
<#if title??>
${title} - 
<#else>
BuildBrighton Badge Workshop
</#if>
</title>
<script src="http://code.jquery.com/jquery-1.5.js"></script>
<script src="js/workshop.js"></script>
</head>
<body <#if bodyId??>id="${bodyId}"</#if>>
<div id="wrapper">
<div id="header">
    <div id="masthead">
    <div role="navigation" id="access">
                <div class="skip-link screen-reader-text"><a title="Skip to content" href="#content">Skip to content</a></div>
                    <div class="menu"><ul><li class="current_page_item"><a title="Home" href="/">Home</a></li></ul></div>
    </div><!-- #access -->
    
    
        <div role="banner" id="branding">
                            <h1 id="site-title">
                <span>
                    <a rel="home" title="BuildBrighton Hackspace" href="/">BuildBrighton Hackspace</a>
                </span>
            </h1>
            <div id="site-description">An electronics, technology and arts workshop in Brighton, UK</div>
           <a rel="home" title="BuildBrighton Hackspace" href="/">
                    <img height="206" width="305" alt="BuildBrighton logo" src="images/bb-banner.png" class=" colorbox-manual">
                    </a>
        </div><!-- #branding -->

    </div><!-- #masthead -->
</div>
<div id="main">
<div id="container">
<div id="content" role="main">
<!-- End header.ftl -->