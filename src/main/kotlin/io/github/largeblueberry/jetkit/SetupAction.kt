package io.github.largeblueberry.jetkit

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile

class SetupAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        // 1. 현재 열려 있는 프로젝트 정보 가져오기
        val project = e.project ?: return

        // 2. 프로젝트의 루트 디렉토리 찾기
        val projectDir: VirtualFile? = project.guessProjectDir()

        // 3. build.gradle.kts 파일 찾기
        val gradleFile = projectDir?.findChild("build.gradle.kts")

        if (gradleFile != null) {
            // 4. 파일 내용 읽기 (테스트용)
            val content = String(gradleFile.contentsToByteArray())

            Messages.showInfoMessage(
                "파일을 찾았습니다!\n글자 수: ${content.length}자",
                "JetKit 분석 결과"
            )
        } else {
            Messages.showErrorDialog("build.gradle.kts 파일을 찾을 수 없습니다.", "에러")
        }
    }
}
