# Figma component to Compose mapping

Expressive APIs are marked `@ExperimentalMaterial3ExpressiveApi`. Check the
Compose Multiplatform material3 version you use, since some APIs land in
the multiplatform artifact later than in Jetpack Compose.

| Figma component | Compose material3 |
|---|---|
| Button (Filled, Tonal, Elevated, Outlined, Text), XS to XL | `Button`, `FilledTonalButton`, `ElevatedButton`, `OutlinedButton`, `TextButton` with `ButtonDefaults.ExtraSmallContainerHeight` ... `ExtraLargeContainerHeight` and `ButtonDefaults.shape` / `squareShape` |
| Icon Button | `IconButton`, `FilledIconButton`, `FilledTonalIconButton`, `OutlinedIconButton` (sizes via `IconButtonDefaults`) |
| FAB, Extended FAB | `FloatingActionButton`, `MediumFloatingActionButton`, `LargeFloatingActionButton`, `ExtendedFloatingActionButton` |
| FAB Menu | `FloatingActionButtonMenu` + `ToggleFloatingActionButton` + `FloatingActionButtonMenuItem` |
| Connected Button Group Item | `ButtonGroup` with `ToggleButton` (connected shapes via `ButtonGroupDefaults.connectedLeadingButtonShapes()` etc.) |
| Split Button | `SplitButtonLayout` with `SplitButtonDefaults.LeadingButton` / `TrailingButton` |
| Chip | `AssistChip`, `FilterChip`, `InputChip`, `SuggestionChip` |
| Switch, Checkbox, Radio | `Switch` (with thumbContent icon), `Checkbox`, `TriStateCheckbox`, `RadioButton` |
| Slider | `Slider` with `SliderDefaults.Track` / `Thumb` (expressive sizes via `SliderDefaults`) |
| Linear and Circular Progress (wavy) | `LinearWavyProgressIndicator`, `CircularWavyProgressIndicator` |
| Loading Indicator | `LoadingIndicator`, `ContainedLoadingIndicator` |
| Badge | `Badge`, `BadgedBox` |
| Text Field | `TextField`, `OutlinedTextField` |
| Search Bar | `SearchBar` / `DockedSearchBar` with `SearchBarDefaults.InputField` |
| Card | `ElevatedCard`, `Card`, `OutlinedCard` |
| List Item | `ListItem` (use `SegmentedListItem` for expressive lists) |
| Tab | `PrimaryTabRow`, `SecondaryTabRow`, `Tab` |
| Dialog | `AlertDialog`, `BasicAlertDialog` |
| Menu | `DropdownMenu`, `DropdownMenuItem` |
| Tooltip | `TooltipBox` with `PlainTooltip` / `RichTooltip` |
| Snackbar | `SnackbarHost`, `Snackbar` |
| Top App Bar (Small, Medium, Large) | `TopAppBar`, `MediumFlexibleTopAppBar`, `LargeFlexibleTopAppBar` |
| Navigation Bar | `ShortNavigationBar` / `NavigationBar` with `ShortNavigationBarItem` |
| Navigation Rail | `WideNavigationRail` / `NavigationRail` |
| Navigation Drawer | `ModalNavigationDrawer`, `ModalDrawerSheet`, `NavigationDrawerItem` |
| Toolbar | `HorizontalFloatingToolbar`, `BottomAppBar` (docked) |
| Bottom Sheet, Side Sheet | `ModalBottomSheet`, `BottomSheetScaffold` (side sheet: custom `Surface` + `ModalNavigationDrawer`) |
| Carousel | `HorizontalMultiBrowseCarousel`, `HorizontalUncontainedCarousel`, `HorizontalCenteredHeroCarousel` |
| Date Picker, Time Picker | `DatePicker`, `DatePickerDialog`, `TimePicker`, `TimePickerDialog` |

## Responsive layout

Use `currentWindowAdaptiveInfo().windowSizeClass`:
- Compact width (phone): `ShortNavigationBar` at the bottom, Large/Medium top app bar.
- Medium and Expanded width (tablet, desktop, web): `WideNavigationRail` on the left, or a top row with `ButtonGroup` as in the desktop design.
- Or use `NavigationSuiteScaffold` to switch automatically.

## Icons

The design uses Material Symbols Rounded. In Compose use
`compose.materialIconsExtended` (`Icons.Rounded.*`) or bundle the Material
Symbols font and render ligatures.
