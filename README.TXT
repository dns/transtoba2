This is the README file for transtoba2 Version 0.23 (beta)

INTRODUCTION
--------------------------------------------------------------------------------
written by leander seige, linguistics by dr. uli kozok
released under the gnu general public license version 3
Copyright (C) 2008 leander Seige and dr. uli kozok
www: http://transtoba2.seige.net
mail: transtoba2 @ seige.net or uli @ bahasa.net

LICENSE
--------------------------------------------------------------------------------
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program.  If not, see http://www.gnu.org/licenses/

DESCRIPTION
--------------------------------------------------------------------------------
The program should be runnable either as a standalone application or embedded
in a web page. For both you need to have an installed JRE (java runtime
environment) of version 1.5 or later. If you don't already have it please look
at the documentation of your operating system how to install a JRE. Make sure
that the java plugin is registered in your webbrowser when installing a JRE.

This program is provided including the source code. You may modify and/or
redistribute it under the terms of the GNU General Public License (see above).
If you improve the program and/or fix bugs we kindly ask you to send us a copy
of the new version.

CHANGELOG
--------------------------------------------------------------------------------
13.05.09     0.23    - new indonesian prefilter table 

28.08.08     0.22    - disabled the look&feel switch, in order have space for
		       switching fontsizes (nice menu bar planned)
		     - fixed a bug with the parameters
22.08.08     0.21    - bigger default font size
25.07.08     0.20    - implemented a simple caching algorithm to speed up the
		       transliteration of longer texts (250 word-slots)
		     - changed the appearance of the progress bar

19.07.08     0.19    - some color modifications (gui)

19.07.08     0.18    - uniform height for gui elements

19.07.08     0.17    - added icons for language selection, thanks go out to
                       www.icondrawer.com for providing free flag icons
                     - removing a bug tripled the speed
                     - added a progress bar for transliteraion of long texts
                     - added a new combobox to switch the look&feel, in the hope
                       that we can find out why copy&paste doesn't work on mac
                     - included liberationsans-regular.ttf (a GPL Version 2 font
		       Liberation Sans, see https://www.redhat.com/promo/fonts/)

15.07.08     0.16    - explicitly set swing cross platform look&feel, trying
                       to enable copy'n'paste under macosx
		     - changed label text to batak / indonesian to make clear
		       that input text can also be batak

14.07.08     0.15    - multilingual gui, applet parameter gui_lang accepts 
                       "en", "id" or "de"
		     - changed german prefilter

14.07.08     0.14    - some gui design work

13.07.08     0.13    - new ttf file once again
                     - removed ugly default borders from jscrollpanes

12.07.08     0.12    - removed fallback-catch and let pass everything, results
                       in major speed improvement
		     - new font, sans-serif (some signs reused from the GPL font
		       Liberation Sans, see https://www.redhat.com/promo/fonts/)
		     - added scrollbars

11.07.08     0.11    - added new font, containing a lot of new int. signs)
                     - much better ("clean"? ;-) unicode 16bit support
		     - added a new diacritic from karo batak, replacing
		       certain occurences of H  

10.07.08     0.10    - added version number to ttf file name in order to force
                       the jre to reload instead of using an old cached version

09.07.08     0.9     - a new ttf font, added new rules for ny(u) and c, modified
                       the warping of the diacritics           

09.07.08     0.8     - a new ttf font, ability to use roman-style punctuation

29.06.08     0.7     - initial release
