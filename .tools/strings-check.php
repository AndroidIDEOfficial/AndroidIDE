#!/usr/bin/php -q
<?php

// Android translation helper tool
// cross checks two strings.xml files to find diffs
//
// usage: ./strings-check.php values/base.xml values-lang/translated.xml
//
// Written by Marcin Orlowski <mail (@) marcinorlowski (.) com>
//
// 2010-07-03: Initial release
// 2017-04-25: Code cleanup; Added exit code

class Convert {

	public function XmlToArray( $n )
	{
		$xml_array = array();
		$occurance = array();

		foreach($n->childNodes as $nc) {
			if (isset($occurance[$nc->nodeName])) {
				$occurance[$nc->nodeName]++;
			} else {
				$occurance[$nc->nodeName] = 1;
			}
		}

		foreach($n->childNodes as $nc) {
			if( $nc->hasChildNodes() ) {
				$childNodes = $nc->childNodes;
				$children = $childNodes->length;

				if ($children>1) {
					$xml_array[$nc->nodeName][] = $this->XmlToArray($nc);

					$counter = count($xml_array[$nc->nodeName])-1;

					$attrs = $nc->attributes;
					for ($k = 0; $k < $attrs->length; $k++) {
						$xml_array[$nc->nodeName][$counter]['attribute'][$attrs->item($k)->nodeName] = $attrs->item($k)->nodeValue;
					}
					//$counter++;
				} else {
					$xml_array[$nc->nodeName][]['cdata'] = $nc->nodeValue;
					$counter = count($xml_array[$nc->nodeName])-1;
					$attrs = $nc->attributes;
					for ($k = 0; $k < $attrs->length; $k++) {
						$xml_array[$nc->nodeName][$counter]['attribute'][$attrs->item($k)->nodeName] = $attrs->item($k)->nodeValue;
					}
				}
			} else {
				if( $nc->hasAttributes() ) {
					$attrs = $nc->attributes;
					for ($k = 0; $k < $attrs->length; $k++) {
						$xml_array[$nc->nodeName][0]['attribute'][$attrs->item($k)->nodeName]= $attrs->item($k)->nodeValue;
					}
				}
			}
		 }

		 return $xml_array;
	}

	public function GetLabels( $fileName )
	{
		$xml = new DOMDocument('1.0','UTF-8');
		$xml->load( $fileName );

		$data = $this->XmlToArray( $xml );

		$result = array();

		foreach( $data['resources'][0]['string'] AS $entry ) {
			$tmp = sprintf("%s", trim($entry['attribute']['name']));
			$result[$tmp] = $tmp;
		}

		return $result;
	}

// class convert
}


	if( $_SERVER['argc'] != 3 ) {
		printf("Usage: %s strings-BASE.xml string-LANG.xml\n", $_SERVER['argv'][0] );
		die( "*** Aborted\n");
	}

	$fileBase = $_SERVER['argv'][1];
	$fileLang = $_SERVER['argv'][2];

	if( file_exists( $fileBase ) == FALSE ) {
		die( sprintf("*** Missing BASE file '%s'\n", $fileBase ));
	}

	if( file_exists( $fileLang ) == FALSE ) {
		die( sprintf("*** Missing LANG file '%s'\n", $fileLang ) );
	}


	$convert = new Convert();
	$dataLang = $convert->GetLabels( $fileLang );
	$dataBase = $convert->GetLabels( $fileBase );

	$cntLang = $cntBase = 0;

	echo "\n";

	// comparing
	printf("Missing in LANG (You need to translate these)\n");
    	printf("File: %s\n", $fileLang);
	printf("------------------------- BEGIN ----------------------\n");
	foreach( $dataBase as $string ) {
		if( !array_key_exists($string, $dataLang)) {
			printf("%s\n", $string);
			$cntLang++;
		}
	}
	printf("-------------------------- END -----------------------\n");

	echo "\n\n";
	printf("Not present in BASE (you need to remove it from LANG)\n");
	printf("File: %s\n", $fileBase);
	printf("------------------------- BEGIN ----------------------\n");
	foreach( $dataLang as $string ) {
		if( !array_key_exists($string, $dataBase)) {
			printf("%s\n", $string);
			$cntBase++;
		}
	}
	printf("-------------------------- END -----------------------\n");

	echo "\n\nSummary\n----------------\n";

	echo "BASE file: '{$fileBase}'\n";
	echo "LANG file: '{$fileLang}'\n";
	echo "\n";

	$rc = 1;
	if( ($cntLang == 0) && ($cntBase==0) ) {
		echo "OK. Files seem to be up to date.\n";
		$rc = 0;
	} else {
		if( $cntLang > 0 ) {
			printf( "%4d missing strings.\n", $cntLang );
		}

		if( $cntBase > 0 ) {
			printf("%4d orphaned strings.\n", $cntBase );
		}
	}

	echo "\n";

	exit($rc);
