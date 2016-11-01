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
		 * It is important that the Registry and Ordering strings of the CMap file
		 *  match those of the CIDFont file with which it works.
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

	public String toString(){
		// Use this method to return the CMap File.
		return sbCmap.toString();
		
	}
}
