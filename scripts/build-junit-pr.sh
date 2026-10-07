#!/usr/bin/env bash
# Builds JUnit from PR #6127 (junit-team/junit-framework) and puts all its Maven artifacts into ./junit-repo with the
# version 6.2.0-pr6127-SNAPSHOT, which pom.xml uses. Nothing is installed into ~/.m2.
#
# Requirements: git, JDK 25 in JAVA_HOME (JUnit's own Gradle build needs it; Gradle provisions the other toolchains).
#
# Environment (all optional):
#   JUNIT_SOURCE  git repository to fetch from      (default: https://github.com/junit-team/junit-framework.git)
#   JUNIT_REF     ref or commit to build            (default: refs/pull/6127/head)
#   GRADLE_ARGS   extra Gradle arguments, e.g. "-Porg.gradle.java.installations.auto-download=false"
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
SOURCE="${JUNIT_SOURCE:-https://github.com/junit-team/junit-framework.git}"
REF="${JUNIT_REF:-refs/pull/6127/head}"
VERSION="6.2.0-pr6127-SNAPSHOT"
WT="$ROOT/work/junit-framework"
G=(git -c core.longpaths=true -c advice.detachedHead=false)

[ -n "${JAVA_HOME:-}" ] || { echo "JAVA_HOME must point to JDK 25" >&2; exit 2; }

if [ ! -d "$WT/.git" ]; then
	mkdir -p "$WT"
	"${G[@]}" -C "$WT" init --quiet
	"${G[@]}" -C "$WT" config core.longpaths true
	"${G[@]}" -C "$WT" config core.autocrlf false
fi
echo "fetching $REF from $SOURCE"
"${G[@]}" -C "$WT" fetch --quiet --depth 1 "$SOURCE" "$REF"
"${G[@]}" -C "$WT" checkout --quiet --force --detach FETCH_HEAD
"${G[@]}" -C "$WT" clean -fdq
SHA="$("${G[@]}" -C "$WT" rev-parse HEAD)"
echo "building JUnit $VERSION from $SHA (takes a few minutes)"

(
	cd "$WT"
	# shellcheck disable=SC2086
	./gradlew --no-daemon --console=plain --no-build-cache -Pversion="$VERSION" \
		-Pjunit.develocity.predictiveTestSelection.enabled=false \
		${GRADLE_ARGS:-} \
		:platform-tooling-support-tests:normalizeMavenRepo
) >"$ROOT/work/build-junit.log" 2>&1 || { tail -n 30 "$ROOT/work/build-junit.log" >&2; exit 1; }

rm -rf "$ROOT/junit-repo"
cp -R "$WT/platform-tooling-support-tests/build/normalized-repo" "$ROOT/junit-repo"
printf 'source=%s\nref=%s\nsha=%s\nversion=%s\n' "$SOURCE" "$REF" "$SHA" "$VERSION" >"$ROOT/junit-repo/BUILD-INFO"
echo "done: $ROOT/junit-repo (PR head $SHA)"
