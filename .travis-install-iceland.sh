#!/bin/bash -e

function install_github() {
	local slug="$1"
	local branch="$2"
	git clone -q --depth=1 -b "${branch}"  "https://github.com/${slug}.git" "${slug}"
	mvn -q -ff -T1.0C -f "${slug}" clean install -DskipTests
}

install_github "52North/iceland" develop
