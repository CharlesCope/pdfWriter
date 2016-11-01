package Cmaps;
/**
 * @author Charles Cope
 * This Cmap is used as a \ToUnicode Cmap for true type fonts.
 * Stand-Alone CMap File
 * Based on  Adobe CMap and CIDFont
 * Files Specification Version 1.0    11 June 1993
 */
public class identityH {
	final private String JavaNewLine = System.getProperty("line.separator");
	private StringBuilder sbCmap = new StringBuilder();

	/**
	 * The constructors  for the class
	 */
	public identityH(){
		CommetHeader();
		InitializingCIDProcset();
		BeginCMapResourceDictionary();
		BeginCmap();
		VersionControlInformation();
		FormallyDefineResources();
		ExtendedUniqueID();
		WritingModeDictionary();
		CodeSpaceRanges();
		CodeMappingRanges();
		EndCmap();
		EncodingDefinesVMResource();
		// close the InitializingCIDProcset (begin)
		EndOperator();
		// close the BeginCMapResourceDictionary (begin)
		EndOperator();
		EndResourceComment();
		EndOfFileComment();
	}
	private void CommetHeader(){
		/** A CMap file must begin with the comment characters %!; otherwise it may
		 * not be given the appropriate handling in some operating system environments.
		 * The first line in the file is	 */
		
		sbCmap.append("%!PS-Adobe-3.0 Resource-CMap");
		
		/** The remainder of the line (after the %!) identifies that file as a CMap resource
		 *   that conforms to the PostScript language document structuring conventions version 3.0. 
		 *   In VM, the CMap uses a procset from a system support file named CIDInit.
		 *   %%DocumentNeededResources indicates that an external resource is
		 *   needed by this document; in this case, the procset CIDInit. %%IncludeResource
		 *   tells any handling software that if the resource is not available on the
		 *   PostScript interpreter, it should be included in-line if possible. */
		sbCmap.append("%%DocumentNeededResources: ProcSet (CIDInit)"+ JavaNewLine);
		sbCmap.append("%%IncludeResource: ProcSet (CIDInit)"+ JavaNewLine);
		/** The %%BeginResource comment informs spoolers and resource managers
		 *   that the information that follows is a resource. There is a corresponding
		 *   %%EndResource comment at the end of the file. The %%BeginResource
		 *   line also states the type of resource (CMap) and its name (Identity-H). */
		sbCmap.append("%%BeginResource: CMap (Identity-H)"+ JavaNewLine);
		/** The %%Title comment has the following structure:
		 *   %Title: (<CMapName> <registry> <ordering> <supplement>) */
		sbCmap.append("%%Title: (Identity-H Adobe Identity 0)"+ JavaNewLine);
		sbCmap.append("%%Version: 10.003"+ JavaNewLine);
		
		// Copyright Information
		sbCmap.append("%%Copyright: -----------------------------------------------------------"+ JavaNewLine);
		sbCmap.append("%%Copyright: Copyright 1990-2009 Adobe Systems Incorporated."+JavaNewLine);
		sbCmap.append("%%Copyright: All rights reserved."+JavaNewLine);
		sbCmap.append("%%Copyright:"+JavaNewLine);
		sbCmap.append("%%Copyright: Redistribution and use in source and binary forms, with or"+ JavaNewLine);
		sbCmap.append("%%Copyright: without modification, are permitted provided that the"+ JavaNewLine);
		sbCmap.append("%%Copyright: following conditions are met:"+ JavaNewLine);
		sbCmap.append("%%Copyright:"+ JavaNewLine);
		sbCmap.append("%%Copyright: Redistributions of source code must retain the above"+ JavaNewLine);
		sbCmap.append("%%Copyright: copyright notice, this list of conditions and the following"+ JavaNewLine);
		sbCmap.append("%%Copyright: disclaimer."+ JavaNewLine);
		sbCmap.append("%%Copyright:"+ JavaNewLine);
		sbCmap.append("%%Copyright: Redistributions in binary form must reproduce the above"+ JavaNewLine);
		sbCmap.append("%%Copyright: copyright notice, this list of conditions and the following"+ JavaNewLine);
		sbCmap.append("%%Copyright: disclaimer in the documentation and/or other materials"+ JavaNewLine);
		sbCmap.append("%%Copyright: provided with the distribution. "+ JavaNewLine);
		sbCmap.append("%%Copyright:"+ JavaNewLine);
		sbCmap.append("%%Copyright: Neither the name of Adobe Systems Incorporated nor the names"+ JavaNewLine);
		sbCmap.append("%%Copyright: of its contributors may be used to endorse or promote"+ JavaNewLine);
		sbCmap.append("%%Copyright: products derived from this software without specific prior"+ JavaNewLine);
		sbCmap.append("%%Copyright: written permission."+ JavaNewLine);
		sbCmap.append("%%Copyright:"+ JavaNewLine);
		sbCmap.append("%%Copyright: THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND"+ JavaNewLine);
		sbCmap.append("%%Copyright: CONTRIBUTORS 'AS IS' AND ANY EXPRESS OR IMPLIED WARRANTIES,"+ JavaNewLine);
		sbCmap.append("%%Copyright: INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF"+ JavaNewLine);
		sbCmap.append("%%Copyright: MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE"+ JavaNewLine);
		sbCmap.append("%%Copyright: DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR"+ JavaNewLine);
		sbCmap.append("%%Copyright: CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,"+ JavaNewLine);
		sbCmap.append("%%Copyright: SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT"+ JavaNewLine);
		sbCmap.append("%%Copyright: NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;"+ JavaNewLine);
		sbCmap.append("%%Copyright: LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)"+ JavaNewLine);
		sbCmap.append("%%Copyright: HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN"+ JavaNewLine);
		sbCmap.append("%%Copyright: CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR"+ JavaNewLine);
		sbCmap.append("%%Copyright: OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS"+ JavaNewLine);
		sbCmap.append("%%Copyright: SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE."+ JavaNewLine);
		sbCmap.append("%%Copyright: -----------------------------------------------------------"+ JavaNewLine);
		sbCmap.append("%%EndComments"+ JavaNewLine);
		// keep it human readable
		sbCmap.append(""+ JavaNewLine);
		
	}
	
	private void InitializingCIDProcset(){
		/** Immediately after the header information and before the definition of the
		 *   CMap proper, a findresource operation is run on the file CIDInit, which is
		 *   one of the system support files installed in the file system. This ensures that
		 *   the routines necessary to process CMap files are first read into VM. An end
		 *   operator corresponding to this begin appears near the end of the file. */
		sbCmap.append("/CIDInit /ProcSet findresource begin"+ JavaNewLine);
		// Keep it human readable
		sbCmap.append(""+ JavaNewLine);
	}
	
	private void BeginCMapResourceDictionary(){
		/** After the CID procset has been initialized, the file defines a PostScript language
		 *   resource instance whose underlying type is a dictionary.
		 *   The line  (12 dict begin ) begins this dictionary. 
		 *   The line that uses the operator defineresource near
		 *   the end of the file registers the CMap as a resource instance.
		 *   The 12 to the best of my understanding is to allocate 12 elements to dictionary.
		 *   This is five more elements than are consumed to avoid a dicfull error on Level 1 interpreters.
		 */
		sbCmap.append("12 dict begin"+ JavaNewLine);
		// Keep it human readable
		sbCmap.append(""+ JavaNewLine);
	}

	private void BeginCmap(){
		/** The CMap is begun with operator (begincmap)
		 *   There is a corresponding endcmap operator near the end of the file
		 *   that completes the task of building the resource. */
		sbCmap.append("begincmap"+ JavaNewLine);
		// Keep it human readable
		sbCmap.append(""+ JavaNewLine);
	}

	private void VersionControlInformation(){
		/** The first of the dictionary objects is CIDSystemInfo. It contains the version control information:
		 *   It is important that the Registry and Ordering strings of the CMap file
		 *   match those of the CIDFont file with which it works.
		 *   Version control information consists of two string values and one integer
		 *   The strings are Registry and Ordering. The integer is Supplement
		 */
		sbCmap.append("/CIDSystemInfo 3 dict dup begin"+ JavaNewLine);
		sbCmap.append("/Registry (Adobe) def"+ JavaNewLine);
		sbCmap.append("/Ordering (Identity) def"+ JavaNewLine);
		sbCmap.append("/Supplement 0 def"+ JavaNewLine);
		sbCmap.append("end def"+ JavaNewLine);
		// Keep it human readable
		sbCmap.append(""+ JavaNewLine);
	}

	private void FormallyDefineResources(){
		/** The line beginning with CMapName formally defines the name of the CMap file.
		 *   Adobe strongly recommends that this be the same name used in the %%Title comment.
		 *   The line beginning with CMapVersion formally defines the version number of this CIDFont file.
		 *   If present, this must be the same version number used in the %%Version comment.
		 *   The line beginning with CMapType defines changes to the internal organization of CMap files or the semantics of CMap operators.
		 *    The CMapName and CMapType are required to be present in the CMap file;
		 *    the CMapVersion is optional. */
		
		sbCmap.append("/CMapName /Identity-H def"+ JavaNewLine);
		sbCmap.append("/CMapVersion 10.003 def"+ JavaNewLine);
		sbCmap.append("/CMapType 1 def"+ JavaNewLine);
		// Keep it human readable
		sbCmap.append(""+ JavaNewLine);
	}
	
	private void ExtendedUniqueID(){
		/** An XUID (extended unique ID) is an entry whose value is an array of integers.
		 *  This array identifies a font by the entire sequence of numbers in the array.
		 *  The first element of an XUID array must be a unique organization identifier, assigned by Adobe Systems.
		 *   I assume the rest of the numbers are also assigned by Adobe but that just my guess.
		 */
		sbCmap.append("/XUID [1 10 25404 9999] def"+ JavaNewLine);
		// Keep it human readable
		sbCmap.append(""+ JavaNewLine);
	}
	
	private void WritingModeDictionary(){
		/** The WMode dictionary entry controls whether the CID-keyed font writes horizontally or vertically
		 *   An entry of 0 defines horizontal writing from left to right;
		 *   An entry of 1 defines vertical writing from top to bottom
		 */
		sbCmap.append("/WMode 0 def"+ JavaNewLine);
		// Keep it human readable
		sbCmap.append(""+ JavaNewLine);
	}
	
	private void CodeSpaceRanges(){
		/** The codespace definition unambiguously specifies which input codes consist of one byte,
		 *   which consist of two, and so forth. The definition of codespace must precede any code mappings,
		 *   including any notdefs— this is one of the few strict organizational requirements of the CMap file
		 *   Input codes may consist of one, two, three, or more hexadecimal bytes, expressed between < > brackets,
		 *   Ranges need not be contiguous, but cannot overlap.
		 *   The codespace entries themselves consist of pairs of hexadecimal numbers in the form <low-end> <high-end>.
		 *   A set of codespace ranges can have up to and including 100 definition lines.
		 *   If a CMap requires more than 100 lines to define its codespace ranges, it can use several sets of 100 or fewer.
		 *   
		 */
		sbCmap.append("1 begincodespacerange"+ JavaNewLine);
		sbCmap.append("  <0000> <FFFF>"+ JavaNewLine);
		sbCmap.append("endcodespacerange"+ JavaNewLine);
		// Keep it human readable
		sbCmap.append(""+ JavaNewLine);
		
	}
	
	private void CodeMappingRanges(){
		/**A set of code mapping ranges can have up to and including 100 definition lines.
		 * When more than 100 are required, the CMap uses several sets. 
		 * The first line of each mapping states how many sets of input codes and starting CIDs there are in the range
		 * The operator begincidrange starts the code mapping for up to 100 ranges.
		 * In the Example below 100 begincidrange states that there will be 100 definition lines
		 * if less ranges are needed  the number should match the operator definition.
		 * The starting and ending input codes appear as hexadecimal strings expressed within <> brackets; 
		 * the (Character Identifier )CID is a decimal number with no brackets.
		 * Example:
		 * 						100 begincidrange
		 *							 <20> <7e> 1
		 * There are 94 input codes between <20> and <7E>. Because the starting CIDis (decimal) 1, 
		 *  input code <20> corresponds to character ID 1, <21> corresponds to 2, <22> corresponds to 3, and so forth.
		 *  Input code <7E> corresponds to character ID 94.
		 *  The operator endcidrange finishes code mapping for ranges of input. */
		
		
		sbCmap.append("100 begincidrange"+ JavaNewLine);
		sbCmap.append("<0000> <00ff> 0"+ JavaNewLine);
		sbCmap.append("<0100> <01ff> 256"+ JavaNewLine);
		sbCmap.append("<0200> <02ff> 512"+ JavaNewLine);
		sbCmap.append("<0300> <03ff> 768"+ JavaNewLine);
		sbCmap.append("<0400> <04ff> 1024"+ JavaNewLine);
		sbCmap.append("<0500> <05ff> 1280"+ JavaNewLine);
		sbCmap.append("<0600> <06ff> 1536"+ JavaNewLine);
		sbCmap.append("<0700> <07ff> 1792"+ JavaNewLine);
		sbCmap.append("<0800> <08ff> 2048"+ JavaNewLine);
		sbCmap.append("<0900> <09ff> 2304"+ JavaNewLine);
		sbCmap.append("<0a00> <0aff> 2560"+ JavaNewLine);
		sbCmap.append("<0b00> <0bff> 2816"+ JavaNewLine);
		sbCmap.append("<0c00> <0cff> 3072"+ JavaNewLine);
		sbCmap.append("<0d00> <0dff> 3328"+ JavaNewLine);
		sbCmap.append("<0e00> <0eff> 3584"+ JavaNewLine);
		sbCmap.append("<0f00> <0fff> 3840"+ JavaNewLine);
		sbCmap.append("<1000> <10ff> 4096"+ JavaNewLine);
		sbCmap.append("<1100> <11ff> 4352"+ JavaNewLine);
		sbCmap.append("<1200> <12ff> 4608"+ JavaNewLine);
		sbCmap.append("<1300> <13ff> 4864"+ JavaNewLine);
		sbCmap.append("<1400> <14ff> 5120"+ JavaNewLine);
		sbCmap.append("<1500> <15ff> 5376"+ JavaNewLine);
		sbCmap.append("<1600> <16ff> 5632"+ JavaNewLine);
		sbCmap.append("<1700> <17ff> 5888"+ JavaNewLine);
		sbCmap.append("<1800> <18ff> 6144"+ JavaNewLine);
		sbCmap.append("<1900> <19ff> 6400"+ JavaNewLine);
		sbCmap.append("<1a00> <1aff> 6656"+ JavaNewLine);
		sbCmap.append("<1b00> <1bff> 6912"+ JavaNewLine);
		sbCmap.append("<1c00> <1cff> 7168"+ JavaNewLine);
		sbCmap.append("<1d00> <1dff> 7424"+ JavaNewLine);
		sbCmap.append("<1e00> <1eff> 7680"+ JavaNewLine);
		sbCmap.append("<1f00> <1fff> 7936"+ JavaNewLine);
		sbCmap.append("<2000> <20ff> 8192"+ JavaNewLine);
		sbCmap.append("<2100> <21ff> 8448"+ JavaNewLine);
		sbCmap.append("<2200> <22ff> 8704"+ JavaNewLine);
		sbCmap.append("<2300> <23ff> 8960"+ JavaNewLine);
		sbCmap.append("<2400> <24ff> 9216"+ JavaNewLine);
		sbCmap.append("<2500> <25ff> 9472"+ JavaNewLine);
		sbCmap.append("<2600> <26ff> 9728"+ JavaNewLine);
		sbCmap.append("<2700> <27ff> 9984"+ JavaNewLine);
		sbCmap.append("<2800> <28ff> 10240"+ JavaNewLine);
		sbCmap.append("<2900> <29ff> 10496"+ JavaNewLine);
		sbCmap.append("<2a00> <2aff> 10752"+ JavaNewLine);
		sbCmap.append("<2b00> <2bff> 11008"+ JavaNewLine);
		sbCmap.append("<2c00> <2cff> 11264"+ JavaNewLine);
		sbCmap.append("<2d00> <2dff> 11520"+ JavaNewLine);
		sbCmap.append("<2e00> <2eff> 11776"+ JavaNewLine);
		sbCmap.append("<2f00> <2fff> 12032"+ JavaNewLine);
		sbCmap.append("<3000> <30ff> 12288"+ JavaNewLine);
		sbCmap.append("<3100> <31ff> 12544"+ JavaNewLine);
		sbCmap.append("<3200> <32ff> 12800"+ JavaNewLine);
		sbCmap.append("<3300> <33ff> 13056"+ JavaNewLine);
		sbCmap.append("<3400> <34ff> 13312"+ JavaNewLine);
		sbCmap.append("<3500> <35ff> 13568"+ JavaNewLine);
		sbCmap.append("<3600> <36ff> 13824"+ JavaNewLine);
		sbCmap.append("<3700> <37ff> 14080"+ JavaNewLine);
		sbCmap.append("<3800> <38ff> 14336"+ JavaNewLine);
		sbCmap.append("<3900> <39ff> 14592"+ JavaNewLine);
		sbCmap.append("<3a00> <3aff> 14848"+ JavaNewLine);
		sbCmap.append("<3b00> <3bff> 15104"+ JavaNewLine);
		sbCmap.append("<3c00> <3cff> 15360"+ JavaNewLine);
		sbCmap.append("<3d00> <3dff> 15616"+ JavaNewLine);
		sbCmap.append("<3e00> <3eff> 15872"+ JavaNewLine);
		sbCmap.append("<3f00> <3fff> 16128"+ JavaNewLine);
		sbCmap.append("<4000> <40ff> 16384"+ JavaNewLine);
		sbCmap.append("<4100> <41ff> 16640"+ JavaNewLine);
		sbCmap.append("<4200> <42ff> 16896"+ JavaNewLine);
		sbCmap.append("<4300> <43ff> 17152"+ JavaNewLine);
		sbCmap.append("<4400> <44ff> 17408"+ JavaNewLine);
		sbCmap.append("<4500> <45ff> 17664"+ JavaNewLine);
		sbCmap.append("<4600> <46ff> 17920"+ JavaNewLine);
		sbCmap.append("<4700> <47ff> 18176"+ JavaNewLine);
		sbCmap.append("<4800> <48ff> 18432"+ JavaNewLine);
		sbCmap.append("<4900> <49ff> 18688"+ JavaNewLine);
		sbCmap.append("<4a00> <4aff> 18944"+ JavaNewLine);
		sbCmap.append("<4b00> <4bff> 19200"+ JavaNewLine);
		sbCmap.append("<4c00> <4cff> 19456"+ JavaNewLine);
		sbCmap.append("<4d00> <4dff> 19712"+ JavaNewLine);
		sbCmap.append("<4e00> <4eff> 19968"+ JavaNewLine);
		sbCmap.append("<4f00> <4fff> 20224"+ JavaNewLine);
		sbCmap.append("<5000> <50ff> 20480"+ JavaNewLine);
		sbCmap.append("<5100> <51ff> 20736"+ JavaNewLine);
		sbCmap.append("<5200> <52ff> 20992"+ JavaNewLine);
		sbCmap.append("<5300> <53ff> 21248"+ JavaNewLine);
		sbCmap.append("<5400> <54ff> 21504"+ JavaNewLine);
		sbCmap.append("<5500> <55ff> 21760"+ JavaNewLine);
		sbCmap.append("<5600> <56ff> 22016"+ JavaNewLine);
		sbCmap.append("<5700> <57ff> 22272"+ JavaNewLine);
		sbCmap.append("<5800> <58ff> 22528"+ JavaNewLine);
		sbCmap.append("<5900> <59ff> 22784"+ JavaNewLine);
		sbCmap.append("<5a00> <5aff> 23040"+ JavaNewLine);
		sbCmap.append("<5b00> <5bff> 23296"+ JavaNewLine);
		sbCmap.append("<5c00> <5cff> 23552"+ JavaNewLine);
		sbCmap.append("<5d00> <5dff> 23808"+ JavaNewLine);
		sbCmap.append("<5e00> <5eff> 24064"+ JavaNewLine);
		sbCmap.append("<5f00> <5fff> 24320"+ JavaNewLine);
		sbCmap.append("<6000> <60ff> 24576"+ JavaNewLine);
		sbCmap.append("<6100> <61ff> 24832"+ JavaNewLine);
		sbCmap.append("<6200> <62ff> 25088"+ JavaNewLine);
		sbCmap.append("<6300> <63ff> 25344"+ JavaNewLine);
		sbCmap.append("endcidrange"+ JavaNewLine);
		// Keep it human readable
		sbCmap.append(""+ JavaNewLine);

		sbCmap.append("100 begincidrange"+ JavaNewLine);
		sbCmap.append("<6400> <64ff> 25600"+ JavaNewLine);
		sbCmap.append("<6500> <65ff> 25856"+ JavaNewLine);
		sbCmap.append("<6600> <66ff> 26112"+ JavaNewLine);
		sbCmap.append("<6700> <67ff> 26368"+ JavaNewLine);
		sbCmap.append("<6800> <68ff> 26624"+ JavaNewLine);
		sbCmap.append("<6900> <69ff> 26880"+ JavaNewLine);
		sbCmap.append("<6a00> <6aff> 27136"+ JavaNewLine);
		sbCmap.append("<6b00> <6bff> 27392"+ JavaNewLine);
		sbCmap.append("<6c00> <6cff> 27648"+ JavaNewLine);
		sbCmap.append("<6d00> <6dff> 27904"+ JavaNewLine);
		sbCmap.append("<6e00> <6eff> 28160"+ JavaNewLine);
		sbCmap.append("<6f00> <6fff> 28416"+ JavaNewLine);
		sbCmap.append("<7000> <70ff> 28672"+ JavaNewLine);
		sbCmap.append("<7100> <71ff> 28928"+ JavaNewLine);
		sbCmap.append("<7200> <72ff> 29184"+ JavaNewLine);
		sbCmap.append("<7300> <73ff> 29440"+ JavaNewLine);
		sbCmap.append("<7400> <74ff> 29696"+ JavaNewLine);
		sbCmap.append("<7500> <75ff> 29952"+ JavaNewLine);
		sbCmap.append("<7600> <76ff> 30208"+ JavaNewLine);
		sbCmap.append("<7700> <77ff> 30464"+ JavaNewLine);
		sbCmap.append("<7800> <78ff> 30720"+ JavaNewLine);
		sbCmap.append("<7900> <79ff> 30976"+ JavaNewLine);
		sbCmap.append("<7a00> <7aff> 31232"+ JavaNewLine);
		sbCmap.append("<7b00> <7bff> 31488"+ JavaNewLine);
		sbCmap.append("<7c00> <7cff> 31744"+ JavaNewLine);
		sbCmap.append("<7d00> <7dff> 32000"+ JavaNewLine);
		sbCmap.append("<7e00> <7eff> 32256"+ JavaNewLine);
		sbCmap.append("<7f00> <7fff> 32512"+ JavaNewLine);
		sbCmap.append("<8000> <80ff> 32768"+ JavaNewLine);
		sbCmap.append("<8100> <81ff> 33024"+ JavaNewLine);
		sbCmap.append("<8200> <82ff> 33280"+ JavaNewLine);
		sbCmap.append("<8300> <83ff> 33536"+ JavaNewLine);
		sbCmap.append("<8400> <84ff> 33792"+ JavaNewLine);
		sbCmap.append("<8500> <85ff> 34048"+ JavaNewLine);
		sbCmap.append("<8600> <86ff> 34304"+ JavaNewLine);
		sbCmap.append("<8700> <87ff> 34560"+ JavaNewLine);
		sbCmap.append("<8800> <88ff> 34816"+ JavaNewLine);
		sbCmap.append("<8900> <89ff> 35072"+ JavaNewLine);
		sbCmap.append("<8a00> <8aff> 35328"+ JavaNewLine);
		sbCmap.append("<8b00> <8bff> 35584"+ JavaNewLine);
		sbCmap.append("<8c00> <8cff> 35840"+ JavaNewLine);
		sbCmap.append("<8d00> <8dff> 36096"+ JavaNewLine);
		sbCmap.append("<8e00> <8eff> 36352"+ JavaNewLine);
		sbCmap.append("<8f00> <8fff> 36608"+ JavaNewLine);
		sbCmap.append("<9000> <90ff> 36864"+ JavaNewLine);
		sbCmap.append("<9100> <91ff> 37120"+ JavaNewLine);
		sbCmap.append("<9200> <92ff> 37376"+ JavaNewLine);
		sbCmap.append("<9300> <93ff> 37632"+ JavaNewLine);
		sbCmap.append("<9400> <94ff> 37888"+ JavaNewLine);
		sbCmap.append("<9500> <95ff> 38144"+ JavaNewLine);
		sbCmap.append("<9600> <96ff> 38400"+ JavaNewLine);
		sbCmap.append("<9700> <97ff> 38656"+ JavaNewLine);
		sbCmap.append("<9800> <98ff> 38912"+ JavaNewLine);
		sbCmap.append("<9900> <99ff> 39168"+ JavaNewLine);
		sbCmap.append("<9a00> <9aff> 39424"+ JavaNewLine);
		sbCmap.append("<9b00> <9bff> 39680"+ JavaNewLine);
		sbCmap.append("<9c00> <9cff> 39936"+ JavaNewLine);
		sbCmap.append("<9d00> <9dff> 40192"+ JavaNewLine);
		sbCmap.append("<9e00> <9eff> 40448"+ JavaNewLine);
		sbCmap.append("<9f00> <9fff> 40704"+ JavaNewLine);
		sbCmap.append("<a000> <a0ff> 40960"+ JavaNewLine);
		sbCmap.append("<a100> <a1ff> 41216"+ JavaNewLine);
		sbCmap.append("<a200> <a2ff> 41472"+ JavaNewLine);
		sbCmap.append("<a300> <a3ff> 41728"+ JavaNewLine);
		sbCmap.append("<a400> <a4ff> 41984"+ JavaNewLine);
		sbCmap.append("<a500> <a5ff> 42240"+ JavaNewLine);
		sbCmap.append("<a600> <a6ff> 42496"+ JavaNewLine);
		sbCmap.append("<a700> <a7ff> 42752"+ JavaNewLine);
		sbCmap.append("<a800> <a8ff> 43008"+ JavaNewLine);
		sbCmap.append("<a900> <a9ff> 43264"+ JavaNewLine);
		sbCmap.append("<aa00> <aaff> 43520"+ JavaNewLine);
		sbCmap.append("<ab00> <abff> 43776"+ JavaNewLine);
		sbCmap.append("<ac00> <acff> 44032"+ JavaNewLine);
		sbCmap.append("<ad00> <adff> 44288"+ JavaNewLine);
		sbCmap.append("<ae00> <aeff> 44544"+ JavaNewLine);
		sbCmap.append("<af00> <afff> 44800"+ JavaNewLine);
		sbCmap.append("<b000> <b0ff> 45056"+ JavaNewLine);
		sbCmap.append("<b100> <b1ff> 45312"+ JavaNewLine);
		sbCmap.append("<b200> <b2ff> 45568"+ JavaNewLine);
		sbCmap.append("<b300> <b3ff> 45824"+ JavaNewLine);
		sbCmap.append("<b400> <b4ff> 46080"+ JavaNewLine);
		sbCmap.append("<b500> <b5ff> 46336"+ JavaNewLine);
		sbCmap.append("<b600> <b6ff> 46592"+ JavaNewLine);
		sbCmap.append("<b700> <b7ff> 46848"+ JavaNewLine);
		sbCmap.append("<b800> <b8ff> 47104"+ JavaNewLine);
		sbCmap.append("<b900> <b9ff> 47360"+ JavaNewLine);
		sbCmap.append("<ba00> <baff> 47616"+ JavaNewLine);
		sbCmap.append("<bb00> <bbff> 47872"+ JavaNewLine);
		sbCmap.append("<bc00> <bcff> 48128"+ JavaNewLine);
		sbCmap.append("<bd00> <bdff> 48384"+ JavaNewLine);
		sbCmap.append("<be00> <beff> 48640"+ JavaNewLine);
		sbCmap.append("<bf00> <bfff> 48896"+ JavaNewLine);
		sbCmap.append("<c000> <c0ff> 49152"+ JavaNewLine);
		sbCmap.append("<c100> <c1ff> 49408"+ JavaNewLine);
		sbCmap.append("<c200> <c2ff> 49664"+ JavaNewLine);
		sbCmap.append("<c300> <c3ff> 49920"+ JavaNewLine);
		sbCmap.append("<c400> <c4ff> 50176"+ JavaNewLine);
		sbCmap.append("<c500> <c5ff> 50432"+ JavaNewLine);
		sbCmap.append("<c600> <c6ff> 50688"+ JavaNewLine);
		sbCmap.append("<c700> <c7ff> 50944"+ JavaNewLine);
		sbCmap.append("endcidrange"+ JavaNewLine);	
		// Keep it human readable
		sbCmap.append(""+ JavaNewLine);
		
		sbCmap.append("56 begincidrange"+ JavaNewLine);
		sbCmap.append("<c800> <c8ff> 51200"+ JavaNewLine);
		sbCmap.append("<c900> <c9ff> 51456"+ JavaNewLine);
		sbCmap.append("<ca00> <caff> 51712"+ JavaNewLine);
		sbCmap.append("<cb00> <cbff> 51968"+ JavaNewLine);
		sbCmap.append("<cc00> <ccff> 52224"+ JavaNewLine);
		sbCmap.append("<cd00> <cdff> 52480"+ JavaNewLine);
		sbCmap.append("<ce00> <ceff> 52736"+ JavaNewLine);
		sbCmap.append("<cf00> <cfff> 52992"+ JavaNewLine);
		sbCmap.append("<d000> <d0ff> 53248"+ JavaNewLine);
		sbCmap.append("<d100> <d1ff> 53504"+ JavaNewLine);
		sbCmap.append("<d200> <d2ff> 53760"+ JavaNewLine);
		sbCmap.append("<d300> <d3ff> 54016"+ JavaNewLine);
		sbCmap.append("<d400> <d4ff> 54272"+ JavaNewLine);
		sbCmap.append("<d500> <d5ff> 54528"+ JavaNewLine);
		sbCmap.append("<d600> <d6ff> 54784"+ JavaNewLine);
		sbCmap.append("<d700> <d7ff> 55040"+ JavaNewLine);
		sbCmap.append("<d800> <d8ff> 55296"+ JavaNewLine);
		sbCmap.append("<d900> <d9ff> 55552"+ JavaNewLine);
		sbCmap.append("<da00> <daff> 55808"+ JavaNewLine);
		sbCmap.append("<db00> <dbff> 56064"+ JavaNewLine);
		sbCmap.append("<dc00> <dcff> 56320"+ JavaNewLine);
		sbCmap.append("<dd00> <ddff> 56576"+ JavaNewLine);
		sbCmap.append("<de00> <deff> 56832"+ JavaNewLine);
		sbCmap.append("<df00> <dfff> 57088"+ JavaNewLine);
		sbCmap.append("<e000> <e0ff> 57344"+ JavaNewLine);
		sbCmap.append("<e100> <e1ff> 57600"+ JavaNewLine);
		sbCmap.append("<e200> <e2ff> 57856"+ JavaNewLine);
		sbCmap.append("<e300> <e3ff> 58112"+ JavaNewLine);
		sbCmap.append("<e400> <e4ff> 58368"+ JavaNewLine);
		sbCmap.append("<e500> <e5ff> 58624"+ JavaNewLine);
		sbCmap.append("<e600> <e6ff> 58880"+ JavaNewLine);
		sbCmap.append("<e700> <e7ff> 59136"+ JavaNewLine);
		sbCmap.append("<e800> <e8ff> 59392"+ JavaNewLine);
		sbCmap.append("<e900> <e9ff> 59648"+ JavaNewLine);
		sbCmap.append("<ea00> <eaff> 59904"+ JavaNewLine);
		sbCmap.append("<eb00> <ebff> 60160"+ JavaNewLine);
		sbCmap.append("<ec00> <ecff> 60416"+ JavaNewLine);
		sbCmap.append("<ed00> <edff> 60672"+ JavaNewLine);
		sbCmap.append("<ee00> <eeff> 60928"+ JavaNewLine);
		sbCmap.append("<ef00> <efff> 61184"+ JavaNewLine);
		sbCmap.append("<f000> <f0ff> 61440"+ JavaNewLine);
		sbCmap.append("<f100> <f1ff> 61696"+ JavaNewLine);
		sbCmap.append("<f200> <f2ff> 61952"+ JavaNewLine);
		sbCmap.append("<f300> <f3ff> 62208"+ JavaNewLine);
		sbCmap.append("<f400> <f4ff> 62464"+ JavaNewLine);
		sbCmap.append("<f500> <f5ff> 62720"+ JavaNewLine);
		sbCmap.append("<f600> <f6ff> 62976"+ JavaNewLine);
		sbCmap.append("<f700> <f7ff> 63232"+ JavaNewLine);
		sbCmap.append("<f800> <f8ff> 63488"+ JavaNewLine);
		sbCmap.append("<f900> <f9ff> 63744"+ JavaNewLine);
		sbCmap.append("<fa00> <faff> 64000"+ JavaNewLine);
		sbCmap.append("<fb00> <fbff> 64256"+ JavaNewLine);
		sbCmap.append("<fc00> <fcff> 64512"+ JavaNewLine);
		sbCmap.append("<fd00> <fdff> 64768"+ JavaNewLine);
		sbCmap.append("<fe00> <feff> 65024"+ JavaNewLine);
		sbCmap.append("<ff00> <ffff> 65280"+ JavaNewLine);
		sbCmap.append("endcidrange"+ JavaNewLine);
		// Keep it human readable
		sbCmap.append(""+ JavaNewLine);
	}
	
	private void EndCmap(){
		/** The CMap is Ended with operator (endcmap)
		 *  This completes the task of building the resource*/
		sbCmap.append("endcmap"+ JavaNewLine);
		// Keep it human readable
		sbCmap.append(""+ JavaNewLine);
	}

	private void EncodingDefinesVMResource(){
		/** Explicitly states the encoding for this CMap file, defines it as a VM resource,
		 *   and pops it from the stack. The argument CMapName is the instance key,
		 *   defined earlier in the file. The argument CMap is the resource category.
		 * 
		 */
		sbCmap.append("CMapName currentdict /CMap defineresource pop"+ JavaNewLine);
		// Keep it human readable
		sbCmap.append(""+ JavaNewLine);
	}
	
	private void EndOperator(){
		/** Use this operator to close any open (begin) operators */
		sbCmap.append("end"+ JavaNewLine);
	}
	
	private void EndResourceComment(){
		/** Comment that defines the end of the file in accordance with the document structuring conventions.
		 *   It is useful if this CMap file is concatenated with other files in a job stream
		 */
		sbCmap.append("%%EndResource"+ JavaNewLine);
	}
	
	private void EndOfFileComment(){
		/** Formally signals the end of the file.	 */
		sbCmap.append("%%EOF"+ JavaNewLine);
	}
	
	public int Length(){
		return sbCmap.length();
		
	}
	public String toString(){
		// Use this method to return the CMap File.
		return sbCmap.toString();
		
	}
}
