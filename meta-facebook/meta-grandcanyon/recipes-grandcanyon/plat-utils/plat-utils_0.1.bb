# Copyright 2020-present Facebook. All Rights Reserved.
#
# This program file is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License as published by the
# Free Software Foundation; version 2 of the License.
#
# This program is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
# for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program in a file named COPYING; if not, write to the
# Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor,
# Boston, MA 02110-1301 USA
SUMMARY = "Utilities"
DESCRIPTION = "Various utilities"
SECTION = "base"
PR = "r1"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

SRC_URI = "file://sol-util \
           file://COPYING \
           file://ast-functions \
           file://power-on.sh \
           file://run_power_on.sh \
          "

pkgdir = "utils"

S = "${WORKDIR}"

# the tools for BMC will be installed in the image
binfiles = " sol-util power-on.sh "

DEPENDS_append = "update-rc.d-native"
RDEPENDS_${PN} += "bash python3 gpiocli "

do_install() {
  # install the package dir
  dst="${D}/usr/local/fbpackages/${pkgdir}"
  install -d $dst

  # install ast-functions
  install -m 644 ast-functions ${dst}/ast-functions

  # create linkages to those binaries
  localbindir="${D}/usr/local/bin"
  install -d ${localbindir}
  for f in ${binfiles}; do
      install -m 755 $f ${dst}/${f}
      ln -s ../fbpackages/${pkgdir}/${f} ${localbindir}/${f}
  done
  
  # init
  install -d ${D}${sysconfdir}/init.d
  install -d ${D}${sysconfdir}/rcS.d
  install -m 755 run_power_on.sh ${D}${sysconfdir}/init.d/run_power_on.sh
  update-rc.d -r ${D} run_power_on.sh start 99 5 .
}

FILES_${PN} += "/usr/local ${sysconfdir}"
