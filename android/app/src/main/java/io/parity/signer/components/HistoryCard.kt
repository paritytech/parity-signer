package io.parity.signer.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import io.parity.signer.models.decode64
import io.parity.signer.models.encodeHex
import io.parity.signer.uniffi.Event
import io.parity.signer.uniffi.VerifierValue

/**
 * Selector for rendering history cards in general list;
 * could easily be moved to backend later: TODO
 */
@Composable
fun HistoryCard(card: Event, timestamp: String) {
	when (card) {
		is Event.DatabaseInitiated -> {
			HistoryCardTemplate(
				image = Icons.Default.Smartphone,
				line1 = timestamp,
				line2 = "Database initiated",
				line3 = ""
			)
		}
		is Event.DeviceWasOnline -> {
			HistoryCardTemplate(
				image = Icons.Default.Dangerous,
				line1 = timestamp,
				line2 = "Device was connected to network",
				line3 = "",
				danger = true
			)
		}
		is Event.GeneralVerifierSet -> {
			card.verifier.v.let {
				if (it is VerifierValue.Standard) {
					HistoryCardTemplate(
						image = Icons.Default.Shield,
						line1 = timestamp,
						line2 = "General verifier set",
						line3 = it.m
					)
				}
			}
		}
		is Event.HistoryCleared -> {
			HistoryCardTemplate(
				image = Icons.Default.DeleteForever,
				line1 = timestamp,
				line2 = "History cleared",
				line3 = ""
			)
		}
		is Event.IdentitiesWiped -> {
			HistoryCardTemplate(
				image = Icons.Default.Delete,
				line1 = timestamp,
				line2 = "All keys were wiped",
				line3 = ""
			)
		}
		is Event.IdentityAdded -> {
			HistoryCardTemplate(
				image = Icons.Default.Pattern,
				line1 = timestamp,
				line2 = "Key created",
				line3 = card.identityHistory.seedName.decode64() + card.identityHistory.path
			)
		}
		is Event.IdentityRemoved -> {
			HistoryCardTemplate(
				image = Icons.Default.Delete,
				line1 = timestamp,
				line2 = "Key removed",
				line3 = card.identityHistory.seedName.decode64() + card.identityHistory.path
			)
		}
		is Event.MessageSignError -> {
			HistoryCardTemplate(
				image = Icons.Default.Warning,
				line1 = timestamp,
				line2 = "Message signing error!",
				line3 = card.signMessageDisplay.message,
				danger = true
			)
		}
		is Event.MessageSigned -> {
			HistoryCardTemplate(
				image = Icons.Default.Done,
				line1 = timestamp,
				line2 = "Generated signature for message",
				line3 = card.signMessageDisplay.message
			)
		}
		is Event.MetadataAdded -> {
			card.metaValuesDisplay.let {
				HistoryCardTemplate(
					image = Icons.Default.QrCodeScanner,
					line1 = timestamp,
					line2 = "Metadata added",
					line3 = it.name + " version " + it.version
				)
			}
		}
		is Event.MetadataRemoved -> {
			card.metaValuesDisplay.let {
				HistoryCardTemplate(
					image = Icons.Default.Delete,
					line1 = timestamp,
					line2 = "Metadata removed",
					line3 = it.name + " version " + it.version
				)
			}
		}
		is Event.NetworkSpecsAdded -> {
			card.networkSpecsDisplay.specs.let {
				HistoryCardTemplate(
					image = Icons.Default.QrCodeScanner,
					line1 = timestamp,
					line2 = "Network added",
					line3 = it.title
				)
			}
		}
		is Event.NetworkSpecsRemoved -> {
			card.networkSpecsDisplay.specs.let {
				HistoryCardTemplate(
					image = Icons.Default.Delete,
					line1 = timestamp,
					line2 = "Network removed",
					line3 = it.title
				)
			}
		}
		is Event.NetworkVerifierSet -> {
			card.networkVerifierDisplay.genesisHash.let {
				HistoryCardTemplate(
					image = Icons.Default.Shield,
					line1 = timestamp,
					line2 = "Network verifier set",
					line3 = it.toUByteArray().toByteArray().encodeHex()
				)
			}
		}
		is Event.ResetDangerRecord -> {
			HistoryCardTemplate(
				image = Icons.Default.DeleteForever,
				line1 = timestamp,
				line2 = "Warnings acknowledged",
				line3 = "",
				danger = true
			)
		}
		is Event.SeedCreated -> {
			HistoryCardTemplate(
				image = Icons.Default.Pattern,
				line1 = timestamp,
				line2 = "Seed created",
				line3 = card.seedCreated.decode64()
			)
		}
		is Event.SeedNameWasShown -> {
			HistoryCardTemplate(
				image = Icons.Default.Warning,
				line1 = timestamp,
				line2 = "Seed was shown",
				line3 = card.seedNameWasShown.decode64()
			)
		}
		is Event.NetworkSpecsSigned -> {
			card.networkSpecsExport.specsToSend.let {
				HistoryCardTemplate(
					image = Icons.Default.Verified,
					line1 = timestamp,
					line2 = "Network specs signed",
					line3 = it.title
				)
			}
		}
		is Event.MetadataSigned -> {
			card.metaValuesExport.let {
				HistoryCardTemplate(
					image = Icons.Default.Verified,
					line1 = timestamp,
					line2 = "Meta signed",
					line3 = it.name + it.version
				)
			}
		}
		is Event.TypesSigned -> {
			HistoryCardTemplate(
				image = Icons.Default.Verified,
				line1 = timestamp,
				line2 = "Types signed",
				line3 = ""
			)
		}
		is Event.SystemEntry -> {
			HistoryCardTemplate(
				image = Icons.Default.Warning,
				line1 = timestamp,
				line2 = "System entry",
				line3 = card.systemEntry
			)
		}
		is Event.TransactionSignError -> {
			HistoryCardTemplate(
				image = Icons.Default.Dangerous,
				line1 = timestamp,
				line2 = "Signing failure",
				line3 = card.signDisplay.userComment.decode64(),
				danger = true
			)
		}
		is Event.TransactionSigned -> {
			HistoryCardTemplate(
				image = Icons.Default.Done,
				line1 = timestamp,
				line2 = "Transaction signed",
				line3 = card.signDisplay.userComment.decode64()
			)
		}
		is Event.TypesAdded -> {
			HistoryCardTemplate(
				image = Icons.Default.QrCodeScanner,
				line1 = timestamp,
				line2 = "New types info loaded",
				line3 = ""
			)
		}
		is Event.TypesRemoved -> {
			HistoryCardTemplate(
				image = Icons.Default.Remove,
				line1 = timestamp,
				line2 = "Types info removed",
				line3 = "",
				danger = true
			)
		}
		is Event.UserEntry -> {
			HistoryCardTemplate(
				image = Icons.Default.Note,
				line1 = timestamp,
				line2 = "User entry",
				line3 = card.userEntry
			)
		}
		is Event.Warning -> {
			HistoryCardTemplate(
				image = Icons.Default.Warning,
				line1 = timestamp,
				line2 = "Warning!",
				line3 = card.warning,
				danger = true
			)
		}
		is Event.WrongPassword -> {
			HistoryCardTemplate(
				image = Icons.Default.Warning,
				line1 = timestamp,
				line2 = "Wrong password entered",
				line3 = "operation declined",
				danger = true
			)
		}
	}
}
