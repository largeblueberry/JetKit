package io.github.largeblueberry.jetkit

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtil

class SetupAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val projectDir = project.guessProjectDir() ?: return
        val gradleFile = projectDir.findChild("build.gradle.kts") ?: return

        // 1. 기존 파일 내용 읽기
        val currentContent = String(gradleFile.contentsToByteArray())

        // 2. 추가할 코드 준비
        val newDependency = "\n\n// Added by JetKit\ndependencies {\n    implementation(\"com.example:jetkit-library:1.0.0\")\n}\n"

        // 3. 파일 수정하기 (WriteCommandAction 필수!)
        WriteCommandAction.runWriteCommandAction(project) {
            try {
                // 기존 내용 끝에 새 코드를 붙여넣습니다.
                VfsUtil.saveText(gradleFile, currentContent + newDependency)

                Messages.showInfoMessage("성공적으로 의존성을 추가했습니다!", "JetKit 완료")
            } catch (ex: Exception) {
                Messages.showErrorDialog("파일 수정 중 오류 발생: ${ex.message}", "에러")
            }
        }
    }
}