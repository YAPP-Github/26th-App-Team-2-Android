package com.yapp.breake.presentation.home.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.HorizontalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.ButtonYellow
import com.yapp.breake.core.designsystem.theme.Gray800
import com.yapp.breake.core.designsystem.util.MultipleEventsCutter
import com.yapp.breake.core.designsystem.util.get
import com.yapp.breake.presentation.home.R

@Composable
internal fun AddButton(
	onAddClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val multipleEventsCutter = remember { MultipleEventsCutter.get() }

	Button(
		shape = RoundedCornerShape(16.dp),
		colors = ButtonDefaults.buttonColors(
			containerColor = ButtonYellow,
			contentColor = Gray800,
		),
		contentPadding = PaddingValues(vertical = 10.5.dp, horizontal = 18.5.dp),
		onClick = { multipleEventsCutter.processEvent(onAddClick) },
		modifier = modifier,
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
		) {
			Icon(
				painter = painterResource(id = R.drawable.ic_plus),
				contentDescription = stringResource(R.string.add_button_content_description),
				modifier = Modifier.size(16.dp),
			)
			HorizontalSpacer(8.dp)
			Text(
				text = stringResource(R.string.add),
				style = BrakeTheme.typography.subtitle16B,
				textAlign = TextAlign.Center,
			)
		}
	}
}

@Composable
@Preview
private fun AddButtonPreview() {
	BrakeTheme {
		AddButton(onAddClick = {})
	}
}
